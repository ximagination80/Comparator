package org.imagination.comparator

trait Mode {
  def onStrict(f: => Unit) = if (this == Strict) f
}
object Strict extends Mode
object Lenient extends Mode