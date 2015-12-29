package comparator

import java.util.regex.Pattern

trait Aliases {
  def set(alias: String, pattern: String): Aliases
  def set(alias: String, pattern: Pattern): Aliases
  def get(alias: String): Option[Pattern]
}

case class AliasesMap() extends Aliases {
  import scala.collection.mutable
  
  private val map = mutable.Map[String,Pattern]()

  def set(alias: String, pattern: String) =
    set(alias, Pattern.compile(pattern))

  def set(alias: String, pattern: Pattern) = {
    map.put(alias, pattern)
    this
  }

  def get(alias: String) = map.get(alias)
}
