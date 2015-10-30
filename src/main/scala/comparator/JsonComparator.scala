package comparator

import java.util
import java.util.regex.Pattern

import com.fasterxml.jackson.databind.{ObjectMapper, JsonNode}
import scala.collection.JavaConversions._

object JsonComparator extends ObjectComparator[JsonNode] {

  case class Element(name: String, node: JsonNode)

  implicit class toScalaElement(itr: util.Iterator[java.util.Map.Entry[java.lang.String, JsonNode]]) {
    def convert: List[Element] = itr.toList.map(e => Element(e.getKey, e.getValue))
  }

  override def compare(expected: JsonNode, actual: JsonNode): Unit = {
    itemCompare(expected,actual)
  }

  def compareNodeRecursive(exList: List[JsonNode],acList: List[JsonNode]): Unit = {
    for ((expected, idx) <- exList.zipWithIndex) {
      acList.lift(idx) match {
        case Some(actual) => itemCompare(expected, actual)
        case None => throw ArrayNotMatchException(s"Index with number $idx not found")
      }
    }

    if (exList.length != acList.length){
      throw ListLengthNotMatch(s"Expected ${exList.length} actual ${acList.length}")
    }
  }

  def itemCompare(expected: JsonNode, actual: JsonNode): Unit = {
    if (expected.isObject) {
      if (!actual.isObject) throw SchemaNotMatchException("Expected object but was " + actual.getNodeType)

      compareRecursive(expected.fields().convert, actual.fields().convert)
    } else if (expected.isArray) {
      if (!actual.isArray) throw SchemaNotMatchException("Expected array but was " + actual.getNodeType)

      import scala.collection.JavaConversions._
      compareNodeRecursive(expected.elements().toList, actual.elements().toList)
    } else {
      assertNodeEqual(expected, actual)
    }
  }

  def compareRecursive(expected: List[Element], real: List[Element]): Unit = {
    if (expected.length != real.length) throw MissingPropertyFound( s"""
           Expected keys| ${expected.map(_.name)}
           Actual   keys| ${real.map(_.name)}
         """)

    for (e <- expected) {
      real.find(aa => aa.name.eq(e.name)) match {
        case Some(actual) => itemCompare(e.node, actual.node)
        case None => throw JsonKeyMissingException(s"Property with name ${e.name} not found")
      }
    }
  }

  import com.fasterxml.jackson.databind.node.JsonNodeType._

  def assertNodeEqual(expectedValue: JsonNode, actualValue: JsonNode) = expectedValue.getNodeType == actualValue.getNodeType match {
    case true =>
      expectedValue.getNodeType match {
        case ARRAY | BINARY | MISSING | POJO | OBJECT | NULL =>
          throw new RuntimeException("Illegal State")

        case BOOLEAN =>
          if (!expectedValue.asBoolean().equals(actualValue.asBoolean()))
            throw JsonPropertyNotMatchException(s"Property ${expectedValue.asText()} is not equal to ${actualValue.asText()}")

        case NUMBER =>
          if (!expectedValue.asDouble().equals(actualValue.asDouble()))
            throw JsonPropertyNotMatchException(s"Property ${expectedValue.asText()} is not equal to ${actualValue.asText()}")

        case STRING =>
          expectedValue.asText().equals("p(.*)") match {
            case true=>
            // ignore this record

            case false=>
              val m = ObjectComparator.pattern.matcher(expectedValue.asText())
              m.matches() match {
                case true =>
                  val matches: Boolean = compile(m.group(1)).matcher(actualValue.asText()).matches()
                  if (!matches) {
                    throw JsonPropertyNotMatchPatternException(
                      s"""
                       Property ${actualValue.asText()} should match pattern ${m.group(1)}
                       as declared in template ${expectedValue.asText()}
                     """)
                  }

                case false =>
                  if (!expectedValue.asText().equals(actualValue.asText()))
                    throw JsonPropertyNotMatchException(s"Property ${expectedValue.asText()} is not equal to ${actualValue.asText()}")
              }
          }
      }
    case false => throw SchemaNotMatchException(s"Expected ${expectedValue.getNodeType} but was ${actualValue.getNodeType}")
  }

  def compile(pattern: String): Pattern = try {
    Pattern.compile(pattern, Pattern.DOTALL)
  } catch {
    case e: Exception => throw new RuntimeException( s"""Illegal Pattern $pattern""")
  }

  case class ListLengthNotMatch(msg: String) extends RuntimeException(msg)
  case class JsonKeyMissingException(msg: String) extends RuntimeException(msg)
  case class ArrayNotMatchException(msg: String) extends RuntimeException(msg)
  case class JsonPropertyNotMatchException(msg: String) extends RuntimeException(msg)
  case class JsonPropertyNotMatchPatternException(msg: String) extends RuntimeException(msg)
  case class MissingPropertyFound(msg: String) extends RuntimeException(msg)
  case class SchemaNotMatchException(msg: String) extends RuntimeException(msg)
}
