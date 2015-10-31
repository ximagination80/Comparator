import java.io.File

import comparator.{ComparisonError, ObjectComparator}
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class EqualityTest extends FunSuite with Matchers with Helper {

  okJson(1)
  okJson(2)
  okJson(3)
  okJson(4)

  errorJson(1,"Difference in properties. Need[Set(enabled, send_notifications_to)]")
  errorJson(2,"Expected 3 actual 2")
  errorJson(3,"Property with name response not found")
  errorJson(4,"Property with name error not found")
  errorJson(5,"Expected array but was OBJECT")
  errorJson(6,"Expected object but was ARRAY")
  errorJson(7,"Expected NUMBER but was STRING")

  okXml(1)
  okXml(2)
  
  errorXml(1,"")
  errorXml(2,"")
  
  /*
    Css
   */

  test(s"Compare css ok 1") {
    compareCss("/ok/1/")
  }

  test(s"Compare css error 1") {
    intercept[ComparisonError] {
      compareCss("/error/1/")
    }
  }

  /*
    Txt
   */

  test(s"Compare txt ok 1") {
    compareTxt("/ok/1/")
  }

  test(s"Compare txt error 2") {
    intercept[ComparisonError] {
      compareTxt("/error/1/")
    }
  }

}

trait Helper {

  this: FunSuite with Matchers =>

  val resDir = new File(System.getProperty("user.dir"), "/src/test/fs")
  require(resDir.exists())

  def file(name: String): File = new File(resDir, name)

  def compare(expected: String, actual: String) = {
    val e = Source.fromFile(file(expected)).mkString
    val a = Source.fromFile(file(actual)).mkString

    ObjectComparator.StringComparator.compare(e, a)
  }

  def compareJson(path: String) = compareType("json", path)
  def compareXml(path: String) = compareType("xml", path)
  def compareCss(path: String) = compareType("css", path)
  def compareTxt(path: String) = compareType("txt", path)

  def errorJson(n: Int, msg: String) = test(s"Compare json error $n") {
    intercept[ComparisonError] {
      compareJson(s"/error/$n/")
    }.msg shouldBe msg
  }

  def errorXml(n: Int, msg: String) = test(s"Compare xml error $n") {
    intercept[ComparisonError] {
      compareXml(s"/error/$n/")
    }.msg shouldBe msg
  }

  def okJson(n: Int) = test(s"Compare json ok $n") {
    compareJson(s"/ok/$n/")
  }

  def okXml(n: Int) = test(s"Compare xml ok $n") {
    compareXml(s"/ok/$n/")
  }

  def compareType(tpe: String, path: String) =
    compare(tpe + path + "expected." + tpe, tpe + path + "actual." + tpe)
}