package comparator

import comparator.ObjectComparator.ComparisonError
import org.w3c.dom._

case class XMLComparator(mode:Mode = STRICT) extends ObjectComparator[Node] {

  def withStrict(f: => Unit) = if (mode == STRICT) f

  @throws[ComparisonError]
  override def compare(exp: Node, act: Node): Unit = {
    if (exp.getNodeName != act.getNodeName)
      throw error(s"Expected node name ${exp.getNodeName} but was ${act.getNodeName}")

    if (exp.hasAttributes) {
      if (!act.hasAttributes)
        throw error(s"Attributes not found in actual document for node ${act.getNodeName}")

      compareAttributes(exp.getAttributes, act.getAttributes)
    } else {
      withStrict{
        if (act.hasAttributes)
          throw error(s"Additional attributes found in actual document for node ${act.getNodeName}")
      }
    }

    StringComparator.compare(exp.getNodeValue, act.getNodeValue)

    if (exp.hasChildNodes){
      if (!act.hasChildNodes) throw error("Child nodes not found")

      compareChildNodes(exp.getChildNodes,act.getChildNodes)
    } else {
      withStrict {
        if (act.hasChildNodes) throw error("Additional child nodes found")
      }
    }
  }

  def compareChildNodes(exp:NodeList,act:NodeList)={
    if (exp.getLength != act.getLength) {
      throw error(s"Child nodes length not equals.")
    }

    for (idx <- 0 until exp.getLength; item = exp.item(idx)) {
      compare(item, act.item(idx))
    }
  }

  def compareAttributes(exp: NamedNodeMap, act: NamedNodeMap) = {
    val e = asList(exp)
    val a = asList(act)

    withStrict{
      if (e.length != a.length) {
        var props = e.map(_._1).toSet -- a.map(_._1)
        if (props.nonEmpty){
          throw error(s"Attributes length not match. Missing property [$props]")
        } else {
          props = a.map(_._1).toSet -- e.map(_._1)
          throw error(s"Attributes length not match. Found unexpected property [$props]")
        }
      }
    }

    e.foreach { e =>
      a.find(_._1 == e._1) match {
        case Some(actual) => StringComparator.compare(e._2, actual._2)
        case None => throw error(s"Attribute with name ${e._1} not found")
      }
    }
  }

  def asList(o: NamedNodeMap): Seq[(String, String)] = {
    for (idx <- 0 until o.getLength; item = o.item(idx))
    yield (item.getNodeName, item.getNodeValue)
  }.toSeq
}

