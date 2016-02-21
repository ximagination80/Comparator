package org.imagination.comparator

trait ObjectComparator[T] {
  @throws[ComparisonError]
  def compare(expected: T, actual: T): Unit
}

case class ComparisonError(msg: String) extends RuntimeException(msg)