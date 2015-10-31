package comparator

import java.io.StringReader
import java.util.regex.Pattern
import javax.xml.parsers.DocumentBuilderFactory

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import comparator.JsonComparator._
import org.w3c.dom.Document
import org.xml.sax.InputSource

trait ObjectComparator[T] {
  def compare(expected: T, actual: T): Unit
}

case class ComparisonError(msg: String) extends RuntimeException(msg)

object ObjectComparator {

  object StringComparator extends ObjectComparator[String] {
    override def compare(inputExpected: String, inputActual: String): Unit = {
      val expected = inputExpected.trim
      val actual = inputActual.trim

      if (expected != actual) {
        if(expected.isEmpty || actual.isEmpty)
          throw new ComparisonError("Content is not equal and can't be associate with json/xml. Unable to compare")

        val parse = tryParse(expected, actual)

        try {
          parse match {
            case JsonObjects(e, a) =>
              JsonComparator.compare(e, a)

            case XMLObjects(e, a) =>
              XMLComparator.compare(e, a)

            case NotEqualContentTypeObjects =>
              throw new ComparisonError("Content is not equal and can't be associate with json/xml. Unable to compare")
          }
        } catch {
          case e: ComparisonError =>
            throw e
          case e: Throwable =>
            throw new ComparisonError(e.getMessage)
        }
      }
    }
  }

  private def tryParse(expected: String, actual: String): ParseResult = {
    asJson(expected) match {
      case Some(e) =>
        asJson(actual) match {
          case Some(a) => JsonObjects(e, a)
          case None => NotEqualContentTypeObjects
        }

      case None =>
        asXML(expected) match {
          case Some(e) =>
            asXML(actual) match {
              case Some(a) => XMLObjects(e, a)
              case None => NotEqualContentTypeObjects
            }
          case None => NotEqualContentTypeObjects
        }
    }
  }

  def asJson(v:String): Option[JsonNode] = {
    val mapper = new ObjectMapper()

    try Some(mapper.readTree(v)) catch {
      case e: Throwable => None
    }
  }

  def asXML(v:String): Option[Document] = {
    val db = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val is = new InputSource(new StringReader(v))

    try Some(db.parse(is)) catch {
      case e: Throwable => None
    }
  }

  sealed trait ParseResult
  case class JsonObjects(expected: JsonNode, actual: JsonNode) extends ParseResult
  case class XMLObjects(expected: Document, actual: Document) extends ParseResult
  object NotEqualContentTypeObjects extends ParseResult
}

object StringValueComparator {
  val any = "p(.*)"
  val patternExtractor = Pattern.compile("^p\\((.*)\\)$", Pattern.MULTILINE)

  def compare(exp: String, act: String):Unit={
    if (exp != act && exp != any) {
      val m = patternExtractor.matcher(exp)
      m.matches() match {
        case true =>
          val matches = compile(m.group(1)).matcher(act).matches()
          if (!matches) throw ComparisonError(
            s"Property $act should match pattern ${m.group(1)}")

        case false =>
          throw ComparisonError(s"Property $exp is not equal to $act")
      }
    }
  }
}
