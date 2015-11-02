package comparator

import java.util.regex.Pattern

import comparator.ObjectComparator.ComparisonError

object StringComparator extends ObjectComparator[String]{

  val any = "p(.*)"
  val patternExtractor = Pattern.compile("^p\\((.*)\\)$", Pattern.MULTILINE)

  @throws[ComparisonError]
  def compare(exp: String, act: String):Unit={
    if (exp != act && exp != any) {
      val m = patternExtractor.matcher(exp)
      m.matches() match {
        case true =>
          val matches = compile(m.group(1)).matcher(act).matches()
          if (!matches) throw error(
            s"Property $act should match pattern ${m.group(1)}")

        case false =>
          throw error(s"Property $exp is not equal to $act")
      }
    }
  }

  def compile(pattern: String): Pattern = try Pattern.compile(pattern, Pattern.DOTALL) catch {
    case e: Exception => throw new RuntimeException( s"Illegal Pattern $pattern")
  }
}
