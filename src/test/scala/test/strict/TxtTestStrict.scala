package test.strict

import org.scalatest._
import matchers.should._
import org.scalatest.funsuite.AnyFunSuite
import test.Helper

class TxtTestStrict extends AnyFunSuite with Matchers with Helper {
  useStrict()

  Seq(
    1 -> "Content doesn't match",
    2 -> "Content doesn't match",
    3 -> "Line Some text 123 should match [a-zA-Z]",
    4 -> "Illegal Pattern [a-z\\/A-Z"
  ).foreach((e) => errorTxt(e._1, e._2))

  (1 to 2) foreach okTxt

}
