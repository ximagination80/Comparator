package org.imagination.comparator

trait ObjectComparator[T] {
  def compare(expected: T, actual: T): Unit
}