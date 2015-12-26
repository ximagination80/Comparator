package test.lenient

import org.scalatest.{FunSuite, Matchers}
import test.Helper

class XmlTestLenient extends FunSuite with Matchers with Helper {
   useLenient()

   Seq(
     1 -> "Attributes not found in actual document for node catalog",
     2 -> "Property Test is not equal to 1",
     3 -> "Property 1 is not equal to 123",
     4 -> "Expected node name catalog but was products",
     5 -> "Expected node name product but was p",
     6 -> "Attributes not found in actual document for node catalog_item",
     7 -> "Child nodes length not equals.",
     8 -> "Child nodes length not equals.",
     9 -> "Child nodes length not equals.",
     10 -> "Child nodes not found",
     11 -> "Attribute with name imagex not found",
     12 -> "Child nodes length not equals.",
     13 -> "Property Large should match pattern [large]+",
     14 -> "Property Red is not equal to red",
     15 -> "Illegal Pattern ([a-z]+"
   ).foreach((e) => errorXml(e._1, e._2))

   (1 to 3) foreach okXml
 }

