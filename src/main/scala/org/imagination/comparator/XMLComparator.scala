package org.imagination.comparator

import org.w3c.dom._

import scala.language.implicitConversions

case class XMLComparator(mode: Mode = Strict)(implicit alias: Alias = AliasMap())
  extends ObjectComparator[Node] with ErrorHelper {

  override def compare(expected: Node, actual: Node) = {
    raise(expected.getNodeName != actual.getNodeName,
      s"Expected node name ${expected.getNodeName} but was ${actual.getNodeName}")

    expected.hasAttributes match {
      case true =>
        raise(!actual.hasAttributes,
          s"Attributes not found in actual document for node ${actual.getNodeName}")
        compareAttributes(expected.getAttributes, actual.getAttributes)

      case false => mode.onStrict(raise(actual.hasAttributes,
        s"Additional attributes found in actual document for node ${actual.getNodeName}"))
    }
    
    StringComparator(mode).compare(expected.getNodeValue, actual.getNodeValue)

    expected.hasChildNodes match {
      case true=>
        raise(!actual.hasChildNodes, "Child nodes not found")
        compareNodes(expected.getChildNodes, actual.getChildNodes)
        
      case false=>
        mode.onStrict(raise(actual.hasChildNodes, "Additional child nodes found"))
    }
  }

  private def compareNodes(expected: NodeList, actual: NodeList) = {
    raise(expected.getLength != actual.getLength, s"Child nodes length not equals.")

    (0 until expected.getLength).foreach { idx =>
      compare(expected.item(idx), actual.item(idx))
    }
  }

  private def compareAttributes(expectedSeq: Seq[(String, String)], actualSeq: Seq[(String, String)]) = {
    mode.onStrict {
      if (expectedSeq.length != actualSeq.length) {
        val props = expectedSeq.map(_._1).toSet -- actualSeq.map(_._1)

        raise(props.nonEmpty, s"Attributes length not match. Missing property [$props]")
        raise(s"Attributes length not match. Found unexpected property [${actualSeq.map(_._1).toSet -- expectedSeq.map(_._1)}]")
      }
    }

    expectedSeq.foreach { e =>
      actualSeq.find(_._1 == e._1).fold(raise(s"Attribute with name ${e._1} not found")) { a =>
        StringComparator(mode).compare(e._2, a._2)
      }
    }
  }

  private implicit def toSeq(o: NamedNodeMap): Seq[(String, String)] =
    for (idx <- 0 until o.getLength; item = o.item(idx)) yield item.getNodeName -> item.getNodeValue
}

