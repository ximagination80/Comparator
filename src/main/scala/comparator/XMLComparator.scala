package comparator

import java.util.regex.Pattern

import org.w3c.dom._

case class XMLComparator(mode:Mode = Strict)(implicit alias:Map[String,Pattern] = Map())
  extends ObjectComparator[Node] with ErrorHelper {

  private def withStrict(f: => Unit) = if (mode == Strict) f

  @throws[ComparisonError]
  override def compare(exp: Node, act: Node): Unit = {
    raise(exp.getNodeName != act.getNodeName,
      s"Expected node name ${exp.getNodeName} but was ${act.getNodeName}")

    if (exp.hasAttributes) {
      raise(!act.hasAttributes,
        s"Attributes not found in actual document for node ${act.getNodeName}")

      compareAttributes(exp.getAttributes, act.getAttributes)
    } else {
      withStrict {
        raise(act.hasAttributes,
          s"Additional attributes found in actual document for node ${act.getNodeName}")
      }
    }

    StringComparator().compare(exp.getNodeValue, act.getNodeValue)

    if (exp.hasChildNodes) {
      raise(!act.hasChildNodes, "Child nodes not found")

      compareChildNodes(exp.getChildNodes, act.getChildNodes)
    } else {
      withStrict {
        raise(act.hasChildNodes, "Additional child nodes found")
      }
    }
  }

  private def compareChildNodes(exp: NodeList, act: NodeList) = {
    raise(exp.getLength != act.getLength, s"Child nodes length not equals.")

    for (idx <- 0 until exp.getLength; item = exp.item(idx)) {
      compare(item, act.item(idx))
    }
  }

  private def compareAttributes(exp: NamedNodeMap, act: NamedNodeMap) = {
    val e = asList(exp)
    val a = asList(act)

    withStrict{
      if (e.length != a.length) {
        val props = e.map(_._1).toSet -- a.map(_._1)

        raise(props.nonEmpty, s"Attributes length not match. Missing property [$props]")
        raise(s"Attributes length not match. Found unexpected property [${a.map(_._1).toSet -- e.map(_._1)}]")
      }
    }

    e.foreach { e =>
      a.find(_._1 == e._1) match {
        case Some(actual) => StringComparator().compare(e._2, actual._2)
        case None => raise(s"Attribute with name ${e._1} not found")
      }
    }
  }

  private def asList(o: NamedNodeMap): Seq[(String, String)] = {
    for (idx <- 0 until o.getLength; item = o.item(idx))
    yield (item.getNodeName, item.getNodeValue)
  }.toSeq
}

