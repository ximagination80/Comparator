package test.lenient

import org.scalatest.{FunSuite, Matchers}
import test.Helper

class TxtTestLenient extends FunSuite with Matchers with Helper {
   useLenient()

   Seq(
     1 -> "Content doesn't match"
   ).foreach((e) => errorTxt(e._1, e._2))

   (1 to 1) foreach okTxt

 }
