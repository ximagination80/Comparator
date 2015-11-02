package test.lenient

import comparator.ObjectComparator._
import comparator.{LENIENT, Comparator, STRICT}
import org.scalatest.{FunSuite, Matchers}

class WrongInputTestLenient extends FunSuite with Matchers {

   def lenient = Comparator(mode = LENIENT)

   test("wrong data 1") {
     intercept[ComparisonError] {
       lenient.compare( """{"a":1}""", """<data><a>1</a></data>""")
     }.msg shouldBe "Content is not equal and can't be associate with json/xml. Unable to compare"
   }

   test("wrong data 2") {
     intercept[ComparisonError] {
       lenient.compare("1", """{"a":1}""")
     }.msg shouldBe "Expected NUMBER but was OBJECT"
   }

   test("wrong data 3") {
     intercept[ComparisonError] {
       lenient.compare("", """{"a":1}""")
     }.msg shouldBe "Content is not equal and can't be associate with json/xml. Unable to compare"
   }

   test("wrong data 4") {
     intercept[ComparisonError] {
       lenient.compare("<x><a></a></x>", """some text""")
     }.msg shouldBe "Content is not equal and can't be associate with json/xml. Unable to compare"
   }
 }
