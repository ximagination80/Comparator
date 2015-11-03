package test.strict

import org.scalatest.{FunSuite, Matchers}
import test.Helper

class JsonTestStrict extends FunSuite with Matchers with Helper {
  useStrict()

  Seq(
    1 -> "Difference in properties or count. Need[Set(enabled, send_notifications_to)]",
    2 -> "Expected array length is 3 actual 2",
    3 -> "Property with name response not found",
    4 -> "Property with name error not found",
    5 -> "Expected ARRAY but was OBJECT",
    6 -> "Expected OBJECT but was ARRAY",
    7 -> "Expected NUMBER but was STRING",
    8 -> "Property with name send_notifications_to not found",
    9 -> "Property false is not equal to true",
    10 -> "Property 10.11 is not equal to 10.1",
    11 -> "Property abs should match pattern \\d+",
    12 -> "Illegal Pattern ([a-z]+",
    13 -> "Expected NULL but was NUMBER"
  ).map((e) => errorJson(e._1, e._2))

  (1 to 5) map okJson

}
