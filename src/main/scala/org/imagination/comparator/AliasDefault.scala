package org.imagination.comparator

import java.util.regex.Pattern
import java.util.regex.Pattern._

trait AliasDefault {

  val aliases = Map[String, Pattern](
    "any" -> any(),

    "digit" -> `digit`(),
    ">0" -> `digit>0`(),
    ">=0" -> `digit>=0`(),
    "<0" -> `digit<0`(),
    "<=0" -> `digit<=0`(),

    "money" -> `money`(),

    "email" -> email(),
    "ip" -> ip(),
    "url" -> url(),
    "uuid" -> uuid(),
    "color" -> color(),

    "y-m-d" -> `y-m-d`(),
    "d-m-y" -> `d-m-y`(),
    "y/m/d" -> `y/m/d`(),
    "d/m/y" -> `d/m/y`()
  )

  def any()        = ".*".p(DOTALL)

  def `digit`()    = s"0|[-+]?$digits".p
  def `digit>=0`() = s"0|[+]?$digits".p
  def `digit>0`()  = s"[+]?$digits".p
  def `digit<=0`() = s"0|-$digits".p
  def `digit<0`()  = s"-$digits".p

  def money()      = "0|[-+]?[1-9]\\d*([,\\.]\\d+)?".p

  def email()      = ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
    "\\@" +
    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
    "(" +
    "\\." +
    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
    ")+"
    ).p

  def ip()         = IP_ADDRESS
  def url()        = WEB_URL
  def uuid()       = "\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}".p
  def color()      = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$".p

  def `y-m-d`()    = ymd("-").p
  def `y/m/d`()    = ymd("/").p
  def `d-m-y`()    = dmy("-").p
  def `d/m/y`()    = dmy("/").p

  private lazy val digits  = "[1-9]\\d*"
  private lazy val day     = "(?:0[1-9]|[12][0-9]|3[01])"
  private lazy val month   = "(?:0[1-9]|1[012])"
  private lazy val year    = "\\d{4}"

  private def ymd(s: String) = year + s + month + s + day
  private def dmy(s: String) = day + s + month + s + year

  private implicit class Compilable(value: String) {
    def p: Pattern = compile(value)
    def p(flag: Int): Pattern = compile(value, flag)
  }

  private lazy val GOOD_IRI_CHAR: String =
    "a-zA-Z0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF"

  private lazy val IP_ADDRESS = (
    "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
      + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
      + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
      + "|[1-9][0-9]|[0-9]))").p

  private lazy val IRI
    = "[" + GOOD_IRI_CHAR + "]([" + GOOD_IRI_CHAR + "\\-]{0,61}[" + GOOD_IRI_CHAR + "]){0,1}"

  private lazy val GOOD_GTLD_CHAR =
    "a-zA-Z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF"

  private lazy val GTLD =
    "[" + GOOD_GTLD_CHAR + "]{2,63}"

  private lazy val HOST_NAME =
    "(" + IRI + "\\.)+" + GTLD

  private lazy val DOMAIN_NAME =
    ("(" + HOST_NAME + "|" + IP_ADDRESS + ")").p

  private lazy val WEB_URL = ("((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
      + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
      + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?"
      + "(?:" + DOMAIN_NAME + ")"
      + "(?:\\:\\d{1,5})?)"
      + "(\\/(?:(?:[" + GOOD_IRI_CHAR + "\\;\\/\\?\\:\\@\\&\\=\\#\\~"
      + "\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?"
      + "(?:\\b|$)").p
}

object AliasDefault extends AliasDefault