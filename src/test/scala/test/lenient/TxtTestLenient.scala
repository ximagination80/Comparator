package test.lenient

import org.scalatest.{FunSuite, Matchers}
import test.Helper

class TxtTestLenient extends FunSuite with Matchers with Helper {
   useLenient()

   Seq(
     1 -> "Content is not equal and can't be associate with json/xml. Unable to compare"
   ).map((e) => errorTxt(e._1, e._2))

   (1 to 1) map okTxt

 }
