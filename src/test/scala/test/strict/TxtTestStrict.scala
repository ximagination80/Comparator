package test.strict

import org.scalatest.{FunSuite, Matchers}
import test.Helper

class TxtTestStrict extends FunSuite with Matchers with Helper {
  useStrict()

  Seq(
//    1 -> "Content doesn't match",
    2 -> "Content doesn't match"
  ).foreach((e) => errorTxt(e._1, e._2))

//  (1 to 1) foreach okTxt

}
