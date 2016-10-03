package org.imagination.comparator

import java.util.regex.Pattern

case class StringLinesComparator(mode: Mode)(implicit alias: Alias = AliasMap())
  extends ObjectComparator[String] with ErrorHelper {

  @throws[MatchException]
  def compare(expected: String, actual: String): Unit = {
    val expectedLines = expected.lines.toList
    val actualLines = actual.lines.toList

    if (expectedLines.size != actualLines.size) {
      raise("Content doesn't match")
    } else {
      expectedLines.zip(actualLines).foreach {
        case (expectedLine, actualLine) =>
          compareLine(expectedLine, actualLine)
      }
    }
  }

  def compareLine(expected: String, actual: String): Unit = {
    if (expected != actual) {
      val pattern = alias.get(expected).getOrElse(compile(expected))
      raise(!pattern.matcher(actual).matches(),
        s"Line $actual should match $expected")
    }
  }

  def compile(pattern: String) = try Pattern.compile(pattern, Pattern.DOTALL) catch {
    case e: Exception => throw new RuntimeException(s"Illegal Pattern $pattern", e)
  }
}