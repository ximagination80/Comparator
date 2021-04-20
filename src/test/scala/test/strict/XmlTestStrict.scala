package test.strict

import test.Helper
import org.scalatest._
import org.scalatest.funsuite.AnyFunSuite
import matchers.should._

class XmlTestStrict extends AnyFunSuite with Matchers with Helper {
  useStrict()

  Seq(
    1 -> "Attributes not found in actual document for node catalog",
    2 -> "Attributes length not match. Missing property [Set(description1)]",
    3 -> "Property 1 is not equal to 123",
    4 -> "Expected node name catalog but was products",
    5 -> "Expected node name product but was p",
    6 -> "Attributes not found in actual document for node catalog_item",
    7 -> "Additional attributes found in actual document for node catalog_item",
    8 -> "Child nodes length not equals.",
    9 -> "Child nodes length not equals.",
    10 -> "Child nodes length not equals.",
    11 -> "Child nodes not found",
    12 -> "Attributes length not match. Missing property [Set(imagex)]",
    13 -> "Child nodes length not equals.",
    14 -> "Attributes length not match. Found unexpected property [Set(gender1)]",
    15 -> "Property Large should match pattern [large]+",
    16 -> "Property Red is not equal to red",
    17 -> "Illegal Pattern ([a-z]+",
    18 -> "Content doesn't match",
    19 -> "Content doesn't match"
  ).foreach((e) => errorXml(e._1, e._2))

  (1 to 2) foreach okXml
}

