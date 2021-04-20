package org.imagination.comparator

import java.io.StringReader
import java.util.regex.Pattern
import java.util.{Map => JMap}
import javax.xml.parsers.DocumentBuilderFactory.newInstance

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.google.gson.Gson
import org.w3c.dom.Document
import org.xml.sax.InputSource

import scala.util.Try

case class Comparator(mode:Mode)(implicit alias:Alias = AliasMap())
  extends ObjectComparator[String] with ErrorHelper{

  override def compare(expected: String, actual: String): Unit = {
    if (expected == actual) return

    raise(expected.isEmpty, "Content doesn't match")
    raise(actual.isEmpty, "Content doesn't match")

    def doesNotMatch() = raise("Content doesn't match")

    def executeUnsafe(): Unit = (expected, actual) match {
      case (Json(e), Json(a)) => JsonComparator(mode).compare(e, a)
      case (Json(_), _) => doesNotMatch()
      case (_, Json(_)) => doesNotMatch()

      case (Xml(e), Xml(a)) => XMLComparator(mode).compare(e, a)
      case (Xml(_), _) => doesNotMatch()
      case (_, Xml(_)) => doesNotMatch()

      case _ => StringLinesComparator(mode).compare(expected, actual)
    }

    try executeUnsafe() catch {
      case e: RuntimeException => raise(e.getMessage)
    }
  }

  object Json {

    private def isJson(string: String): Boolean = try {
      new Gson().fromJson(string, classOf[AnyRef])
      true
    } catch {
      case e: RuntimeException => false
    }

    def unapply(v: String): Option[JsonNode] = Try {
      if (isJson(v)) {
        new ObjectMapper().readTree(v)
      } else {
        throw new RuntimeException("not a json")
      }
    }.toOption
  }

  object Xml {
    def unapply(v: String): Option[Document] = Try {
      newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(v)))
    }.toOption
  }
}

object Comparator {
  def java() = JavaComparator

  def strict(implicit alias: Alias = AliasMap()) = Comparator(mode = Strict)
  def lenient(implicit alias: Alias = AliasMap()) = Comparator(mode = Lenient)
}

object JavaComparator {

  def lenient = Comparator(mode = Lenient)(AliasMap())
  def lenient(map: JMap[String, Pattern]) = Comparator(mode = Lenient)(toAlias(map))

  def strict = Comparator(mode = Strict)(AliasMap())
  def strict(map: JMap[String, Pattern]) =Comparator(mode = Strict)(toAlias(map))

  def toAlias(map: JMap[String, Pattern]): AliasMap = {
    import scala.jdk.CollectionConverters._
    map.asScala.toMap.foldLeft(AliasMap())((map, a) => map.add(a._1, a._2))
  }
}

