package test.lenient

import org.scalatest._
import matchers.should._
import test.Helper
import org.scalatest.funsuite.AnyFunSuite

class TxtTestLenient extends AnyFunSuite with Matchers with Helper {
   useLenient()

   Seq(
     1 -> "Content doesn't match",
     2 -> "Line Some text 123 should match [a-zA-Z]"
   ).foreach((e) => errorTxt(e._1, e._2))

   (1 to 2) foreach okTxt

 }
