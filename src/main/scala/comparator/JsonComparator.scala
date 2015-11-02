package comparator

import java.util.Map.{Entry => JEntry}

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType._
import comparator.ObjectComparator.ComparisonError

import scala.collection.JavaConversions._

case class JsonComparator(mode:Mode = STRICT) extends ObjectComparator[JsonNode] {

  def withStrict(f: => Unit) = if (mode == STRICT) f

  @throws[ComparisonError]
  override def compare(exp: JsonNode, act: JsonNode): Unit = {
    if (exp.isObject) {
      if (!act.isObject) throw error("Expected object but was " + act.getNodeType)

      compareElementList(exp.fields().toList, act.fields().toList)
    } else if (exp.isArray) {
      if (!act.isArray) throw error("Expected array but was " + act.getNodeType)

      compareNodeList(exp.elements().toList, act.elements().toList)
    } else {
      compareNodes(exp, act)
    }
  }

  def compareNodeList(exp: List[JsonNode], act: List[JsonNode]): Unit = {
    if (exp.length != act.length)
      throw error(s"Expected array length is ${exp.length} actual ${act.length}")

    for ((expected, idx) <- exp.zipWithIndex) {
      compare(expected, act(idx))
    }
  }

  def compareElementList(exp: List[JEntry[String, JsonNode]],
                         act: List[JEntry[String, JsonNode]]): Unit = {
    withStrict{
      if (exp.length != act.length) {
        val props = exp.map(_.getKey).toSet -- act.map(_.getKey)
        throw error(s"Difference in properties or count. Need[$props]")
      }
    }

    exp.foreach{ e=>
      act.find(_.getKey == e.getKey) match {
        case Some(a) => compare(e.getValue, a.getValue)
        case None => throw error(s"Property with name ${e.getKey} not found")
      }
    }
  }

  def compareNodes(exp: JsonNode, act: JsonNode) = {
    if (exp.getNodeType != act.getNodeType)
      throw error(s"Expected ${exp.getNodeType} but was ${act.getNodeType}")

    exp.getNodeType match {
      case BOOLEAN =>
        if (exp.asBoolean() != act.asBoolean())
          throw error(s"Property ${exp.asText()} is not equal to ${act.asText()}")

      case NUMBER =>
        if (exp.asDouble() != act.asDouble())
          throw error(s"Property ${exp.asText()} is not equal to ${act.asText()}")

      case STRING =>
        StringComparator.compare(exp.asText(),act.asText())

      case p@_ =>
        throw new RuntimeException("Unexpected json property type. Type is " + p)
    }
  }
}
