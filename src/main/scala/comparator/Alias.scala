package comparator

import java.util.regex.Pattern
import java.util.regex.Pattern.compile

trait Alias {
  def add(alias: String, pattern: String): Alias
  def add(alias: String, pattern: Pattern): Alias
  def get(alias: String): Option[Pattern]
}

case class AliasMap(map: Map[String, Pattern] = Map()) extends Alias {
  def add(alias: String, regexp: String) = 
    add(alias, compile(regexp))
  
  def add(alias: String, pattern: Pattern) = 
    AliasMap(map + (alias -> pattern))
  
  def get(alias: String) = 
    map.get(alias)
}