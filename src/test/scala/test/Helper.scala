package test

import java.io.File

import org.imagination.comparator._
import org.scalatest._
import matchers.should._

import scala.io.Source
import org.scalatest.funsuite.AnyFunSuite

trait Helper {

  this: AnyFunSuite with Matchers =>

  def prepareAliases(): Alias =
    AliasMap().add("date", "\\d{4}-\\d{2}-\\d{2}")

  implicit val aliases = prepareAliases()

  var mode:Mode = _
  def useStrict():Unit = mode = Strict
  def useLenient():Unit = mode = Lenient

  val resDir = new File(System.getProperty("user.dir"), "/src/test/fs")
  require(resDir.exists())

  def file(name: String): File = new File(resDir, name)

  def toString(file: File): String = {
    val s = Source.fromFile(file)
    try s.mkString finally s.close()
  }

  def compare(expected: String, actual: String): Unit = Comparator(mode).compare(
    toString(file(expected)),
    toString(file(actual))
  )

  def compareJson(path: String):Unit = compareType("json", path)
  def compareXml(path: String):Unit = compareType("xml", path)
  def compareCss(path: String):Unit = compareType("css", path)
  def compareTxt(path: String):Unit = compareType("txt", path)

  def errorJson(n: Int, msg: String):Unit = test(s"Json error $n") {
    val actual = intercept[MatchException] {
      compareJson(s"/error/$n/")
    }.msg shouldBe msg
  }

  def errorXml(n: Int, msg: String):Unit = test(s"Xml error $n") {
    intercept[MatchException] {
      compareXml(s"/error/$n/")
    }.msg shouldBe msg
  }

  def errorCss(n: Int, msg: String):Unit = test(s"Css error $n") {
    intercept[MatchException] {
      compareCss(s"/error/$n/")
    }.msg shouldBe msg
  }

  def errorTxt(n: Int, msg: String):Unit = test(s"Txt error $n") {
    intercept[MatchException] {
      compareTxt(s"/error/$n/")
    }.msg shouldBe msg
  }

  def okJson(n: Int):Unit = test(s"Json ok $n") {
    compareJson(s"/ok/$n/")
  }

  def okXml(n: Int):Unit = test(s"Xml ok $n") {
    compareXml(s"/ok/$n/")
  }

  def okCss(n: Int):Unit = test(s"Css ok $n") {
    compareCss(s"/ok/$n/")
  }

  def okTxt(n: Int):Unit = test(s"Txt ok $n") {
    compareTxt(s"/ok/$n/")
  }

  def compareType(tpe: String, path: String):Unit = {
    val template = tpe + {
      mode match {
        case Strict => "/strict/"
        case Lenient => "/lenient/"
      }
    } + path + "%s." + tpe
    compare(template.format("expected"), template.format("actual"))
  }

}
