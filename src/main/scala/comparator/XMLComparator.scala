package comparator

import org.w3c.dom.Document

object XMLComparator extends ObjectComparator[Document] {
  override def compare(expected: Document, actual: Document): Unit = {
    //TODO
  }
}

