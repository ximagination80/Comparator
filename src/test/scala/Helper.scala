import java.io.File

import comparator.{ComparisonError, ObjectComparator}
import org.scalatest.{Matchers, FunSuite}

import scala.io.Source

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

  def errorJson(n: Int, msg: String) = test(s"Json error $n") {
    intercept[ComparisonError] {
      compareJson(s"/error/$n/")
    }.msg shouldBe msg
  }

  def errorXml(n: Int, msg: String) = test(s"Xml error $n") {
    intercept[ComparisonError] {
      compareXml(s"/error/$n/")
    }.msg shouldBe msg
  }

  def errorCss(n: Int, msg: String) = test(s"Css error $n") {
    intercept[ComparisonError] {
      compareCss(s"/error/$n/")
    }.msg shouldBe msg
  }

  def errorTxt(n: Int, msg: String) = test(s"Txt error $n") {
    intercept[ComparisonError] {
      compareTxt(s"/error/$n/")
    }.msg shouldBe msg
  }

  def okJson(n: Int) = test(s"Json ok $n") {
    compareJson(s"/ok/$n/")
  }

  def okXml(n: Int) = test(s"Xml ok $n") {
    compareXml(s"/ok/$n/")
  }

  def okCss(n: Int) = test(s"Css ok $n") {
    compareCss(s"/ok/$n/")
  }

  def okTxt(n: Int) = test(s"Txt ok $n") {
    compareTxt(s"/ok/$n/")
  }

  def compareType(tpe: String, path: String) =
    compare(tpe + path + "expected." + tpe, tpe + path + "actual." + tpe)
}