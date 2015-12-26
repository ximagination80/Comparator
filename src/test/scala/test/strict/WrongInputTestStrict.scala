package test.strict

import comparator.{ComparisonError, Comparator, Strict}
import org.scalatest.{FunSuite, Matchers}

class WrongInputTestStrict extends FunSuite with Matchers {

  def strict:Comparator = Comparator(mode = Strict)

  test("wrong data 1") {
    intercept[ComparisonError] {
      strict.compare( """{"a":1}""", """<data><a>1</a></data>""")
    }.msg shouldBe "Content is not equal and type is not the same. Unable to compare trees."
  }

  test("wrong data 2") {
    intercept[ComparisonError] {
      strict.compare("1", """{"a":1}""")
    }.msg shouldBe "Expected NUMBER but was OBJECT"
  }

  test("wrong data 3") {
    val msg: String = intercept[ComparisonError] {
      strict.compare("", """{"a":1}""")
    }.msg
    msg shouldBe "Content is not equal and type is not the same. Unable to compare trees."
  }

  test("wrong data 4") {
    intercept[ComparisonError] {
      strict.compare("<x><a></a></x>", """some text""")
    }.msg shouldBe "Content is not equal and type is not the same. Unable to compare trees."
  }
}
