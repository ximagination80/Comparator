package comparator

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import org.apache.tika.Tika

trait ObjectComparator {
  def compare(expected: String, actual: String): Unit
}

object ObjectComparator {

  object StringComparator extends ObjectComparator {
    override def compare(expected: String, actual: String): Unit = {
      if (expected != actual) {
        val c1 = getContentType(expected)
        val c2 = getContentType(actual)

        if (c1 != c2) {
          throw new ComparisonError(
            s"""
              Expected content type is $c1
              Actual content type is $c2
            """)
        }

        c1 match {
          case "application/json" => JsonComparator.compare(expected, actual)
          case "application/xml" => XMLComparator.compare(expected, actual)
          case _ => throw new NotEqualError(s"Content is equal by content type $c1 but not equal by string content")
        }
      } 
    }

    def getContentType(source: String) = {
      new Tika().detect(new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)))
    }
  }

  object JsonComparator extends ObjectComparator {
    override def compare(expected: String, actual: String): Unit = {

    }
  }

  object XMLComparator extends ObjectComparator {
    override def compare(expected: String, actual: String): Unit = {

    }
  }

  case class ComparisonError(msg: String) extends RuntimeException(msg)
  case class NotEqualError(msg: String) extends RuntimeException(msg)
}
