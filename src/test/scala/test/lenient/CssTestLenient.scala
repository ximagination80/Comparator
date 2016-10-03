package test.lenient

import org.scalatest.{FunSuite, Matchers}
import test.Helper

class CssTestLenient extends FunSuite with Matchers with Helper {
   useLenient()

   Seq(
     1 -> "Content doesn't match"
   ).foreach((e) => errorCss(e._1, e._2))

   (1 to 1) foreach okCss

 }
