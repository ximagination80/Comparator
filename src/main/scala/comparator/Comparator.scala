package comparator

import java.io.StringReader
import java.util.regex.Pattern
import javax.xml.parsers.DocumentBuilderFactory.newInstance

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.util.{Map => JMap}

case class Comparator(mode:Mode)(implicit alias:Alias = AliasMap())
  extends ObjectComparator[String] with ErrorHelper{

  @throws[ComparisonError]
  override def compare(expected: String, actual: String): Unit = {
    if (expected != actual) {
      raise(expected.isEmpty || actual.isEmpty,
        "Content is not equal and type is not the same. Unable to compare trees.")

      val result = Parser(expected, actual).parse()

      try {
        result match {
          case (Json(e), Json(a)) =>
            JsonComparator(mode).compare(e, a)

          case (Xml(e), Xml(a)) =>
            XMLComparator(mode).compare(e, a)

          case _ =>
            raise("Content is not equal and type is not the same. Unable to compare trees.")
        }
      } catch {
        case e: Throwable => raise(e.getMessage)
      }
    }
  }

  case class Parser(expected: String, actual: String) {
    def parse(): (ParseResult, ParseResult) =
      (toParseResult(expected), toParseResult(actual))

    def toParseResult(value: String): ParseResult =
      asJson(value).orElse(asXML(value)).getOrElse(Unknown(value))
  }

  def asJson(v: String): Option[ParseResult] =
    try Some(Json(new ObjectMapper().readTree(v))) catch {
      case e: Throwable =>
        None
    }

  def asXML(v: String): Option[Xml] =
    try Some(Xml(newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(v))))) catch {
      case e: Throwable =>
        None
    }

  sealed trait ParseResult
  case class Json(value: JsonNode) extends ParseResult
  case class Xml(value: Document) extends ParseResult
  case class Unknown(value:String) extends ParseResult
}

object Comparator {
  def java() = JavaComparator

  def strict(implicit alias: Alias = AliasMap()) =
    Comparator(mode = Strict)

  def lenient(implicit alias: Alias = AliasMap()) =
    Comparator(mode = Lenient)
}

object JavaComparator {
  def lenient(map: JMap[String, Pattern]) =
    Comparator(mode = Lenient)(toAlias(map))

  def lenient =
    Comparator(mode = Lenient)(AliasMap())

  def strict(map: JMap[String, Pattern]) =
    Comparator(mode = Strict)(toAlias(map))

  def strict =
    Comparator(mode = Strict)(AliasMap())

  def toAlias(map: JMap[String, Pattern]): AliasMap = {
    import scala.collection.JavaConversions._
    map.toMap.foldLeft(AliasMap())((map, a) => map.add(a._1, a._2))
  }
}

