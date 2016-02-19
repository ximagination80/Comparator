package comparator

import java.util.regex.Pattern

case class StringComparator()(implicit alias: Alias = AliasMap())
  extends ObjectComparator[String] with ErrorHelper {

  @throws[ComparisonError]
  def compare(expected: String, actual: String) {
    if (expected != actual) {

      extractRegexp(expected) match {
        case Some(aliasOrRawPattern)=>
          val pattern = alias.get(aliasOrRawPattern).getOrElse(compile(aliasOrRawPattern))
          raise(!pattern.matcher(actual).matches(),
            s"Property $actual should match pattern $aliasOrRawPattern")
          
        case None=>
          raise(s"Property $expected is not equal to $actual")
      }
    }
  }

  def compile(pattern: String) = try Pattern.compile(pattern, Pattern.DOTALL) catch {
    case e: Exception => throw new RuntimeException(s"Illegal Pattern $pattern",e)
  }

  def extractRegexp(v: String) =
    if (v.length > 3 && v.startsWith("p(") && v.endsWith(")")) Some(v.substring(2, v.length - 1)) else None
}