package test.lenient

import org.scalatest._
import matchers.should._
import test.Helper
import org.scalatest.funsuite.AnyFunSuite

class CssTestLenient extends AnyFunSuite with Matchers with Helper {
   useLenient()

   Seq(
     1 -> "Content doesn't match"
   ).foreach((e) => errorCss(e._1, e._2))

   (1 to 1) foreach okCss

 }
