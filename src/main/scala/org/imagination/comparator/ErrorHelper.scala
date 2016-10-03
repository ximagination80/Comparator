package org.imagination.comparator

trait ErrorHelper {
  def raise(msg: => String): Unit = raise(flag = true, msg)
  def raise(flag: Boolean, msg: => String): Unit = if (flag) throw MatchException(msg)
}