package comparator

import java.util.Map.{Entry => JEntry}

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType._

import scala.collection.JavaConversions._

case class JsonComparator(mode:Mode = Strict)(implicit alias:Alias = AliasMap())
  extends ObjectComparator[JsonNode] with ErrorHelper {

  private def withStrict(f: => Unit) = if (mode == Strict) f

  @throws[ComparisonError]
  override def compare(exp: JsonNode, act: JsonNode): Unit = {
    raise(exp.getNodeType != act.getNodeType, s"Expected ${exp.getNodeType} but was ${act.getNodeType}")

    (exp.getNodeType: @unchecked) match {
      case BOOLEAN =>
        raise(exp.asBoolean() != act.asBoolean(),
          s"Property ${exp.asText()} is not equal to ${act.asText()}")

      case NUMBER =>
        raise(exp.asDouble() != act.asDouble(),
          s"Property ${exp.asText()} is not equal to ${act.asText()}")

      case STRING =>
        StringComparator().compare(exp.asText(), act.asText())

      case ARRAY =>
        compareNodeList(exp.elements().toList, act.elements().toList)

      case OBJECT =>
        compareElementList(exp.fields().toList, act.fields().toList)

      case NULL =>
      // equals
    }
  }

  private def compareNodeList(exp: List[JsonNode], act: List[JsonNode]): Unit = {
    raise(exp.length != act.length, s"Expected array length is ${exp.length} actual ${act.length}")

    for ((expected, idx) <- exp.zipWithIndex) {
      compare(expected, act(idx))
    }
  }

  private def compareElementList(exp: List[JEntry[String, JsonNode]],
                                 act: List[JEntry[String, JsonNode]]): Unit = {
    withStrict {
      raise(exp.length != act.length,
        s"Difference in properties or count. Need[${exp.map(_.getKey).toSet -- act.map(_.getKey)}]")
    }

    exp.foreach { e =>
      act.find(_.getKey == e.getKey) match {
        case Some(a) =>
          compare(e.getValue, a.getValue)

        case None =>
          raise(s"Property with name ${e.getKey} not found")
      }
    }
  }
}
