package org.imagination.comparator

import java.util.regex.Pattern
import java.util.regex.Pattern.compile

trait Alias {
  def add(alias: String, pattern: String): Alias
  def add(alias: String, pattern: Pattern): Alias
  def get(alias: String): Option[Pattern]
}

case class AliasMap() extends Alias {
  private val map = scala.collection.mutable.Map[String,Pattern]() ++ AliasDefault.aliases

  def add(alias: String, regexp: String): AliasMap =
    add(alias, compile(regexp))

  def add(alias: String, pattern: Pattern):AliasMap = {
    map(alias) = pattern
    this
  }

  def get(alias: String) =
    map.get(alias)
}