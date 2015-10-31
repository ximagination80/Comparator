package comparator

import java.util.Map.{Entry => JEntry}
import java.util.regex.Pattern

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType._

import scala.collection.JavaConversions._

object JsonComparator extends ObjectComparator[JsonNode] {

  override def compare(exp: JsonNode, act: JsonNode): Unit = {
    if (exp.isObject) {
      if (!act.isObject) throw ComparisonError("Expected object but was " + act.getNodeType)

      compareElementList(exp.fields().toList, act.fields().toList)
    } else if (exp.isArray) {
      if (!act.isArray) throw ComparisonError("Expected array but was " + act.getNodeType)

      compareNodeList(exp.elements().toList, act.elements().toList)
    } else {
      compareNodes(exp, act)
    }
  }

  def compareNodeList(exp: List[JsonNode], act: List[JsonNode]): Unit = {
    if (exp.length != act.length)
      throw ComparisonError(s"Expected array length is ${exp.length} actual ${act.length}")

    for ((expected, idx) <- exp.zipWithIndex) {
      act.lift(idx) match {
        case Some(a) => compare(expected, a)
        case None => throw ComparisonError(s"Index with number $idx not found")
      }
    }
  }

  def compareElementList(exp: List[JEntry[String, JsonNode]],
                         act: List[JEntry[String, JsonNode]]): Unit = {
    if (exp.length != act.length) {
      val props = exp.map(_.getKey).toSet -- act.map(_.getKey)
      throw ComparisonError(s"Difference in properties. Need[$props]")
    }

    exp.foreach{ e=>
      act.find(_.getKey == e.getKey) match {
        case Some(a) => compare(e.getValue, a.getValue)
        case None => throw ComparisonError(s"Property with name ${e.getKey} not found")
      }
    }
  }

  def compareNodes(exp: JsonNode, act: JsonNode) = {
    if (exp.getNodeType != act.getNodeType)
      throw ComparisonError(s"Expected ${exp.getNodeType} but was ${act.getNodeType}")

    exp.getNodeType match {
      case BOOLEAN =>
        if (exp.asBoolean() != act.asBoolean())
          throw ComparisonError(s"Property ${exp.asText()} is not equal to ${act.asText()}")

      case NUMBER =>
        if (exp.asDouble() != act.asDouble())
          throw ComparisonError(s"Property ${exp.asText()} is not equal to ${act.asText()}")

      case STRING =>
        if (exp.asText() != act.asText() && exp.asText() != any) {
          val m = patternExtractor.matcher(exp.asText())
          m.matches() match {
            case true =>
              val matches = compile(m.group(1)).matcher(act.asText()).matches()
              if (!matches) throw ComparisonError(
                s"Property ${act.asText()} should match pattern ${m.group(1)} as declared in template ${exp.asText()}")

            case false =>
              throw ComparisonError(s"Property ${exp.asText()} is not equal to ${act.asText()}")
          }
        }
      case p@_ =>
        throw new RuntimeException("Unexpected json property type. Type is " + p)
    }
  }

  def compile(pattern: String): Pattern = try Pattern.compile(pattern, Pattern.DOTALL) catch {
    case e: Exception => throw new RuntimeException( s"Illegal Pattern $pattern")
  }
}
