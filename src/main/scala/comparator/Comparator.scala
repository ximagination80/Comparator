package comparator

import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import comparator.ObjectComparator.ComparisonError
import org.w3c.dom.Document
import org.xml.sax.InputSource

object Comparator extends ObjectComparator[String] {

  @throws[ComparisonError]
  override def compare(expected: String, actual: String): Unit = {
    if (expected != actual) {
      if (expected.isEmpty || actual.isEmpty)
        throw error("Content is not equal and can't be associate with json/xml. Unable to compare")

      val parse = tryParse(expected, actual)

      try {
        parse match {
          case JsonObjects(e, a) =>
            JsonComparator.compare(e, a)

          case XMLObjects(e, a) =>
            XMLComparator.compare(e, a)

          case NotEqualContentTypeObjects =>
            throw error("Content is not equal and can't be associate with json/xml. Unable to compare")
        }
      } catch {
        case e: ComparisonError =>
          throw e
        case e: Throwable =>
          throw error(e.getMessage)
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

  def asJson(v: String): Option[JsonNode] = {
    val mapper = new ObjectMapper()

    try Some(mapper.readTree(v)) catch {
      case e: Throwable => None
    }
  }

  def asXML(v: String): Option[Document] = {
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

