package comparator

import java.io.StringReader
import java.util.regex.Pattern
import javax.xml.parsers.DocumentBuilderFactory.newInstance

import com.fasterxml.jackson.databind.ObjectMapper
import org.xml.sax.InputSource
import java.util.{Map => JMap}

import scala.util.Try

case class Comparator(mode:Mode)(implicit alias:Alias = AliasMap())
  extends ObjectComparator[String] with ErrorHelper{

  @throws[ComparisonError]
  override def compare(expected: String, actual: String): Unit = {
    if (expected != actual) {
      raise(expected.isEmpty || actual.isEmpty,
        "Content is not equal and type is not the same. Unable to compare trees.")

      def executeUnsafe():Unit = (expected, actual) match {
        case (Json(e), Json(a)) => JsonComparator(mode).compare(e, a)
        case (Xml(e), Xml(a)) => XMLComparator(mode).compare(e, a)
        case _ => raise("Content is not equal and type is not the same. Unable to compare trees.")
      }

      try executeUnsafe() catch {
        case e: Throwable => raise(e.getMessage)
      }
    }
  }

  object Json {
    def unapply(v: String) = Try(new ObjectMapper().readTree(v)).toOption
  }
  object Xml {
    def unapply(v: String) = Try(newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(v)))).toOption
  }
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

