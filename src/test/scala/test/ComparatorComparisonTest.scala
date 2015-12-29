package test

import java.util
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

  test("Object companion lenient java support test") {
    val map = newJMap()
    map.put("1",Pattern.compile("\\d+"))

    val cmp = Comparator.java()

    cmp.lenient.mode shouldBe comparator.Lenient
    cmp.lenient(newJMap()).mode shouldBe comparator.Lenient
    cmp.lenient(map).mode shouldBe comparator.Lenient
  }

  test("Object companion strict java support test") {
    val map = newJMap()
    map.put("1",Pattern.compile("\\d+"))

    val cmp = Comparator.java()

    cmp.strict.mode shouldBe comparator.Strict
    cmp.strict(newJMap()).mode shouldBe comparator.Strict
    cmp.strict(map).mode shouldBe comparator.Strict
  }

  test("Comparator to alias check") {
    val regexp = "\\d+"
    val map = newJMap()
    map.put("1", Pattern.compile(regexp))
    map.put("2", Pattern.compile(regexp))
    map.put("3", Pattern.compile(regexp))

    val cmp = Comparator.java()

    val alias = cmp.toAlias(map)

    (1 to 3).map(_.toString).foreach {
      alias.get(_).get.pattern() shouldEqual regexp
    }
  }

  def newJMap():util.HashMap[String,Pattern]=
    new util.HashMap[String,Pattern]()
}
