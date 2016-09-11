package org.imagination.comparator

import java.util.regex.Pattern

case class StringComparator(mode: Mode)(implicit alias: Alias) extends ObjectComparator[String] with ErrorHelper {

  def compare(expected: String, actual: String) {
    if (expected != actual) {

      extractAction(expected) match {
        case PatternComparison(aliasOrRawPattern) =>
          val pattern = alias.get(aliasOrRawPattern).getOrElse(compile(aliasOrRawPattern))
          raise(!pattern.matcher(actual).matches(),
            s"Property $actual should match pattern $aliasOrRawPattern")


        case TreeComparison(tree) =>
          Comparator(mode).compare(tree, actual)

        case Empty =>
          raise(s"Property $expected is not equal to $actual")
      }
    }
  }

  def compile(pattern: String) = try Pattern.compile(pattern, Pattern.DOTALL) catch {
    case e: Exception => throw new RuntimeException(s"Illegal Pattern $pattern", e)
  }

  def extractAction(v: String):Result = {
    if (v.length > 3) {
      if (v.startsWith("p(") && v.endsWith(")"))
        PatternComparison(v.substring(2, v.length - 1))
      else if (v.startsWith("t(") && v.endsWith(")")) {
        TreeComparison(v.substring(2, v.length - 1))
      } else {
        Empty
      }
    } else {
      Empty
    }
  }

  trait Result
  case class PatternComparison(pattern: String) extends Result
  case class TreeComparison(tree: String) extends Result
  object Empty extends Result

}