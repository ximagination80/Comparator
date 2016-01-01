package comparator

import java.util.regex.Pattern

import comparator.PatternExtractor.group

case class StringComparator()(implicit alias: Alias = AliasMap())
  extends ObjectComparator[String] with ErrorHelper {

  @throws[ComparisonError]
  def compare(expected: String, actual: String) {
    if (expected != actual) {
      group(expected) match {

        case Some(aliasOrRawPattern)=>
          val pattern = alias.get(aliasOrRawPattern).getOrElse(compile(aliasOrRawPattern))
          raise(!pattern.matcher(actual).matches(),
            s"Property $actual should match pattern $aliasOrRawPattern")
          
        case None=>
          raise(s"Property $expected is not equal to $actual")
      }
    }
  }

  def compile(pattern: String): Pattern = try Pattern.compile(pattern, Pattern.DOTALL) catch {
    case e: Exception => throw new RuntimeException(s"Illegal Pattern $pattern",e)
  }
}

object PatternExtractor {
  private val extractor = Pattern.compile("^p\\((.*)\\)$", Pattern.MULTILINE)

  def group(v: String): Option[String] = {
    val m = extractor.matcher(v)
    if (m.matches()) Some(m.group(1)) else None
  }
}
