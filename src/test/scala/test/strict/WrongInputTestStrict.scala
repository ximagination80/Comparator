package test.strict

import org.imagination.comparator.{MatchException, Strict, Comparator}
import org.scalatest.{FunSuite, Matchers}

class WrongInputTestStrict extends FunSuite with Matchers {

  def strict:Comparator = Comparator(mode = Strict)

  test("wrong data 1") {
    intercept[MatchException] {
      strict.compare( """{"a":1}""", """<data><a>1</a></data>""")
    }.msg shouldBe "Content doesn't match"
  }

  test("wrong data 2") {
    intercept[MatchException] {
      strict.compare("1", """{"a":1}""")
    }.msg shouldBe "Expected NUMBER but was OBJECT"
  }

  test("wrong data 3") {
    val msg: String = intercept[MatchException] {
      strict.compare("", """{"a":1}""")
    }.msg
    msg shouldBe "Content doesn't match"
  }

  test("wrong data 4") {
    intercept[MatchException] {
      strict.compare("<x><a></a></x>", """some text""")
    }.msg shouldBe "Content doesn't match"
  }
}
