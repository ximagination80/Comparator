package test

import comparator.Comparator
import org.scalatest.{Matchers, FunSuite}

class ComparatorComparisonTest extends FunSuite with Matchers {

  test("Field Comparator.strict == STRICT") {
    Comparator.strict.mode shouldBe comparator.Strict
  }

  test("Field Comparator.lenient == LENIENT") {
    Comparator.lenient.mode shouldBe comparator.Lenient
  }

}
