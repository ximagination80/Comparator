package test

import java.util.regex.Pattern

import comparator.{AliasesMap, Comparator}
import org.scalatest.{FunSuite, Matchers}

class ComparatorComparisonTest extends FunSuite with Matchers {

  test("Field Comparator.strict == STRICT") {
    Comparator.strict.mode shouldBe comparator.Strict
  }

  test("Field Comparator.lenient == LENIENT") {
    Comparator.lenient.mode shouldBe comparator.Lenient
  }

  test("Alias test") {
    val regexp = "\\d+"

    val alias = AliasesMap()
    alias.
      set("1", regexp).
      set("2", regexp).
      set("3", Pattern.compile(regexp))

    alias.get("none") shouldEqual None
    (1 to 3).map(_.toString).foreach {
      alias.get(_).get.pattern() shouldEqual regexp
    }
  }
}
