package test

import java.util.regex.Pattern

import comparator.{AliasMap, Comparator}
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

    val alias = AliasMap().
      add("1", regexp).
      add("2", regexp).
      add("3", Pattern.compile(regexp))

    alias.get("none") shouldEqual None
    (1 to 3).map(_.toString).foreach {
      alias.get(_).get.pattern() shouldEqual regexp
    }
  }

  test("Alias test 2"){
    implicit val aliases = AliasMap().
      add("date","\\d{4}-\\d{2}-\\d{2}").
      add("number", "\\d+")

    Comparator.strict.compare(
      """
    {"date":"p(date)", "cost":"p(number)"}
      """,
      """
    {"date":"2015-11-11", "cost":"100"}
      """)
  }
}
