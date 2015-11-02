package comparator

import comparator.ObjectComparator.ComparisonError

trait ObjectComparator[T] {

  @throws[ComparisonError]
  def compare(expected: T, actual: T): Unit

  def error(msg: String) = ComparisonError(msg)
}

object ObjectComparator {
  case class ComparisonError(msg: String) extends RuntimeException(msg)
}

trait Mode {
  def strict = this == STRICT
  def lenient = this == LENIENT
}
object STRICT extends Mode
object LENIENT extends Mode