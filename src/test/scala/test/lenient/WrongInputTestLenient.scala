package test.lenient

import org.imagination.comparator.{MatchException, Lenient, Comparator}
import org.scalatest._
import matchers.should._
import org.scalatest.funsuite.AnyFunSuite

class WrongInputTestLenient extends AnyFunSuite with Matchers {

   def lenient:Comparator = Comparator(mode = Lenient)

   test("wrong data 1") {
     intercept[MatchException] {
       lenient.compare( """{"a":1}""", """<data><a>1</a></data>""")
     }.msg shouldBe "Content doesn't match"
   }

   test("wrong data 2") {
     intercept[MatchException] {
       lenient.compare("1", """{"a":1}""")
     }.msg shouldBe "Expected NUMBER but was OBJECT"
   }

   test("wrong data 3") {
     intercept[MatchException] {
       lenient.compare("", """{"a":1}""")
     }.msg shouldBe "Content doesn't match"
   }

   test("wrong data 4") {
     intercept[MatchException] {
       lenient.compare("<x><a></a></x>", """some text""")
     }.msg shouldBe "Content doesn't match"
   }
 }
