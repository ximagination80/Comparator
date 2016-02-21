package test

import org.imagination.comparator.{AliasMap, AliasDefault}
import org.scalatest.{FunSuite, Matchers}

class DefaultAliasesTest extends FunSuite with Matchers{

  lazy val featuresMap = AliasDefault.aliases

  test("Including into AliasMap default features") {
    val m = AliasMap()

    featuresMap.keys.foreach { e =>
      m.get(e).getOrElse(
        throw new RuntimeException(s"Feature with key [$e] not appended to AliasMap"))
    }
  }

  Seq(
    any(),
    digit(),
    `>0`(),
    `>=0`(),
    `<0`(),
    `<=0`(),
    money(),
    email(),
    ip(),
    url(),
    uuid(),
    color(),
    `y-m-d`(),
    `d-m-y`(),
    `y/m/d`(),
    `d/m/y`()).foreach { e =>

    val pattern = featuresMap.getOrElse(e.key,
      throw new RuntimeException(s"Object with key [${e.key}}] not found in default aliases map"))

    def execute(seq: Seq[String], view: String, flag: Boolean) =
      seq.foreach { data =>
        test(s"$view  ${e.key}  **  [$data]") {
          pattern.matcher(data).matches() shouldBe flag
        }
      }

    execute(e.`true`, "(+)", flag = true)
    execute(e.`false`, "(-)", flag = false)
  }

  case class Regexp(key: String, `true`: Seq[String], `false`: Seq[String])
  
  def any() = Regexp("any",
    Seq("1","multiline \n \n\r test 123 ~ *!@#$%^&*()_>?<L:}{"),
    Nil)

  def digit() = Regexp("digit",
    Seq("1", "100", "+1", "+1123123131312312312312312311231231313123123123123123",
      "-1", "-1123123131312312312312312311231231313123123123123123",
      "1123123131312312312312312311231231313123123123123123", "0"),
    Seq("abc", "+0", "-0"))

  def `>0`() = Regexp(">0",
    Seq("1", "2", "11234567890123123131453454353453"),
    Seq("-0", "+0", "0", "-1", "-11234567890123123131453454353453"))

  def `>=0`() = Regexp(">=0",
    Seq("0","1","2","11234567890123123131453454353453"),
    Seq("-0","+0","-1","-11234567890123123131453454353453"))
  
  def `<0`() = Regexp("<0",
    Seq("-1","-11234567890123123131453454353453"),
    Seq("-0","+0","0","1","2","11234567890123123131453454353453"))

  def `<=0`() = Regexp("<=0",
    Seq("0","-1","-11234567890123123131453454353453"),
    Seq("-0","+0","1","2","11234567890123123131453454353453"))

  def money() = Regexp("money",
    Seq("100", "-100", "+100", "0",
      "1234567890123456789,12345678901234567890",
      "+1234567890123456789,12345678901234567890",
      "-1234567890123456789,12345678901234567890",
      "1234567890123456789.12345678901234567890",
      "+1234567890123456789.12345678901234567890",
      "-1234567890123456789.12345678901234567890"),
    Seq("-0", "+0",
      "0150", "0150.150", "-0150.150",
      "+0150.150", "+0150,150", "123 123",
      "123 123,123"))

  def email() = Regexp("email",
    Seq(
      "imagination@yahoo.com",
      "imagination-100@yahoo.com",
      "imagination.100@yahoo.com",
      "imagination111@imagination.com",
      "imagination-100@imagination.net",
      "imagination.100@imagination.com.au",
      "imagination@1.com",
      "imagination@gmail.com.com",
      "imagination+100@gmail.com",
      "imagination-100@yahoo-test.com",
      "imagination123@gmail.a",
      ".imagination@imagination.com",
      "imagination..2002@gmail.com",
      "imagination.@gmail.com",
      "imagination@gmail.com.1a"
    ),
    Seq(
      "imagination",
      "imagination@.com.my",
      "imagination123@.com",
      "imagination123@.com.com",
      "imagination()*@gmail.com",
      "imagination@%*.com",
      "imagination@imagination@gmail.com"
    ))

  def ip() = Regexp("ip",
    Seq(
      "1.1.1.1",
      "255.255.255.255",
      "192.168.1.1",
      "10.10.1.1",
      "132.254.111.10",
      "26.10.2.10",
      "127.0.0.1"),
    Seq(
      "10.10.10",
      "10.10",
      "10",
      "a.a.a.a",
      "10.0.0.a",
      "10.10.10.256",
      "222.222.2.999",
      "999.10.10.20",
      "2222.22.22.22",
      "22.2222.22.2"))

  def url() = Regexp("url",
    Seq(
      "https://github.com/ximagination80/Comparator?arg=1&arg2=2",
      "https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html",
      "http://grepcode.com/file/repo1.maven.org/maven2/commons-validator/commons-validator/1.4.0/org/apache/commons/validator/routines/EmailValidator.java#EmailValidator.0EMAIL_REGEX",
      "Http://grepcode.com/file/repo1.maven.org/maven2/commons-validator/commons-validator/1.4.0/org/apache/commons/validator/routines/AbstractCalendarValidator.java?av=f",
      "grepcode.com/file/repo1.maven.org/maven2/commons-validator/commons-validator/1.4.0/org/apache/commons/validator/routines/AbstractCalendarValidator.java?av=f"),
    Seq(
      "file/repo1.maven.org/maven2/commons-validator/commons-validator/1.4.0/org/apache/commons/validator/routines/AbstractCalendarValidator.java?av=f",
      "ftp://grepcode.com/file/repo1.maven.org/maven2/commons-validator/commons-validator/1.4.0/org/apache/commons/validator/routines/AbstractCalendarValidator.java?av=f",
      "file://grepcode.com/file/repo1.maven.org/maven2/commons-validator/commons-validator/1.4.0/org/apache/commons/validator/routines/AbstractCalendarValidator.java?av=f",
      "Http://grepcode",
      "https://grepcode"))

  def uuid() = Regexp("uuid",
    Seq(
      "38400000-8cf0-11bd-b23e-10b96e4ef00d",
      "123e4567-e89b-12d3-a456-426655440000",
      "123E4567-E89B-12D3-A456-426655440000"),

    Seq("384000008cf0-11bd-b23e-10b96e4ef00d",
      "123e4567~e89b~12d3~a456~426655440000",
      "123E4567/E89B/12D3/A456/426655440000",
      "{123E4567-E89B-12D3-A456-42665544000A}",
      "38400000-8cf0-11bd-b23e-10b96e4ef00d}"
    ))

  def color() = Regexp("color",
    Seq("#1f1f1F", "#AFAFAF","#1AFFa1","#222fff", "#F00"),
    Seq("123456","#afafah","#123abce","aFaE3f", "F00","#afaf", "#F0h"	))

  def `y-m-d`() = Regexp("y-m-d",
    Seq("2015-11-01","2015-12-31","9999-12-31","1234-01-01","1234-12-31"),
    Seq("2015-11-1","2015-11-32","2015-13-30","99999-12-31","00000-12-31"))

  def `y/m/d`() = Regexp("y/m/d",
    Seq("2015/11/01","2015/12/31","9999/12/31","1234/01/01","1234/12/31"),
    Seq("2015/11/1","2015/11/32","2015/13/30","99999/12/31","00000/12/31"))

  def `d-m-y`() = Regexp("d-m-y",
    Seq("01-11-2015","31-12-2015","31-12-9999","01-01-1234","31-12-1234"),
    Seq("1-11-2015","32-11-2015","30-13-2015","31-12-99999","31-12-00000"))

  def `d/m/y`() = Regexp("d/m/y",
    Seq("01/11/2015","31/12/2015","31/12/9999","01/01/1234","31/12/1234"),
    Seq("1/11/2015","32/11/2015","30/13/2015","31/12/99999","31/12/00000"))
}