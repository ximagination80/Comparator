import comparator.ComparisonError
import comparator.ObjectComparator._
import org.scalatest.{FunSuite, Matchers}

class WrongInputTest extends FunSuite with Matchers {

  test("wrong data 1") {
    intercept[ComparisonError] {
      StringComparator.compare( """{"a":1}""", """<data><a>1</a></data>""")
    }.msg shouldBe "Content is not equal and can't be associate with json/xml. Unable to compare"
  }

  test("wrong data 2") {
    intercept[ComparisonError] {
      StringComparator.compare("1", """{"a":1}""")
    }.msg shouldBe "Expected NUMBER but was OBJECT"
  }

  test("wrong data 3") {
    intercept[ComparisonError] {
      StringComparator.compare("", """{"a":1}""")
    }.msg shouldBe "Content is not equal and can't be associate with json/xml. Unable to compare"
  }

}
