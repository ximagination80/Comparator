package test

import comparator.Comparator
import org.scalatest.{Matchers, FunSuite}

class ComparatorComparisonTest extends FunSuite with Matchers {

  test("Field Comparator.MODE_STRICT == STRICT") {
    Comparator.MODE_STRICT.mode shouldBe comparator.STRICT
  }

  test("Field Comparator.MODE_LENIENT == LENIENT") {
    Comparator.MODE_LENIENT.mode shouldBe comparator.LENIENT
  }

}
