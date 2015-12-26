package comparator

trait ObjectComparator[T] {

  @throws[ComparisonError]
  def compare(expected: T, actual: T): Unit
  
}

trait ErrorHelper {
  def raise(msg: => String):Unit =
    raise(condition = true, msg)

  def raise(condition: Boolean, msg: => String):Unit =
    if (condition) throw ComparisonError(msg)
}

case class ComparisonError(msg: String) extends RuntimeException(msg)

trait Mode
object Strict extends Mode
object Lenient extends Mode