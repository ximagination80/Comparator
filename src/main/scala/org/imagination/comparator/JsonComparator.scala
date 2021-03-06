package org.imagination.comparator

import java.util.Map.{Entry => JEntry}

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType._

import scala.jdk.CollectionConverters._

case class JsonComparator(mode:Mode = Strict)(implicit alias:Alias = AliasMap())
  extends ObjectComparator[JsonNode] with ErrorHelper {

  @throws[MatchException]
  override def compare(expected: JsonNode, actual: JsonNode): Unit = {
    (expected.getNodeType, actual.getNodeType) match {
      case (BOOLEAN, BOOLEAN) =>
        raise(expected.asBoolean() != actual.asBoolean(),
          s"Property ${expected.asText()} is not equal to ${actual.asText()}")

      case (NUMBER, NUMBER) =>
        raise(expected.asDouble() != actual.asDouble(),
          s"Property ${expected.asText()} is not equal to ${actual.asText()}")

      case (STRING, STRING) | (STRING, NUMBER) | (STRING, BOOLEAN)=>
        StringComparator(mode).compare(expected.asText(), actual.asText())

      case (ARRAY, ARRAY) =>
        compareElements(expected.elements().asScala.toList, actual.elements().asScala.toList)

      case (OBJECT, OBJECT) =>
        compareFields(expected.fields().asScala.toList, actual.fields().asScala.toList)

      case (NULL, NULL) => // equals

      case _ =>
        raise(expected.getNodeType != actual.getNodeType,
          s"Expected ${expected.getNodeType} but was ${actual.getNodeType}")
    }
  }

  def compareElements(expected: List[JsonNode], actual: List[JsonNode]): Unit = {
    raise(expected.length != actual.length, s"Expected array length is ${expected.length} actual ${actual.length}")

    expected.zip(actual).foreach { p =>
      compare(p._1, p._2)
    }
  }

  def compareFields(expected: List[JEntry[String, JsonNode]], actual: List[JEntry[String, JsonNode]]): Unit = {
    mode.onStrict(raise(expected.length != actual.length,
      s"Difference in properties or count. Missing " +
        s"[${(expected.map(_.getKey).toSet -- actual.map(_.getKey).toSet).mkString(",")}]"))

    expected.foreach { e =>
      actual.find(_.getKey == e.getKey).fold(raise(s"Property with name ${e.getKey} not found")) { a =>
        compare(e.getValue, a.getValue)
      }
    }
  }
}
