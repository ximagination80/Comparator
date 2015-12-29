package comparator

import java.util.regex.Pattern

case class StringComparator()(implicit alias: Aliases = AliasesMap())
  extends ObjectComparator[String] with ErrorHelper {

  @throws[ComparisonError]
  def compare(expected: String, actual: String) {
    if (expected != actual && expected != StringComparator.any) {
      val matcher = StringComparator.patternExtractor.matcher(expected)
      matcher.matches() match {
        case true =>
          val aliasOrRawPattern = matcher.group(1)
          val pattern = alias.get(aliasOrRawPattern).getOrElse(compile(aliasOrRawPattern))

          raise(!pattern.matcher(actual).matches(),
            s"Property $actual should match pattern $aliasOrRawPattern")

        case false =>
          raise(s"Property $expected is not equal to $actual")
      }
    }
  }

  def compile(pattern: String): Pattern = try Pattern.compile(pattern, Pattern.DOTALL) catch {
    case e: Exception => throw new RuntimeException(s"Illegal Pattern $pattern")
  }
}

object StringComparator {
  val any = "p(.*)"
  val patternExtractor = Pattern.compile("^p\\((.*)\\)$", Pattern.MULTILINE)
}
