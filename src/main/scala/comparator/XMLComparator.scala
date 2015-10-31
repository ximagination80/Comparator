package comparator

import org.w3c.dom._

object XMLComparator extends ObjectComparator[Node] {
  override def compare(exp: Node, act: Node): Unit = {
    if (exp.getNodeName != act.getNodeName)
      throw new ComparisonError(s"Expected node name ${exp.getNodeName} but was ${act.getNodeName}")

    if (exp.hasAttributes) {
      if (!act.hasAttributes)
        throw new ComparisonError(s"Attributes not found in actual document for node ${act.getNodeName}")

      compareAttributes(exp.getAttributes, act.getAttributes)
    } else {
      if (act.hasAttributes)
        throw new ComparisonError(s"Additional attributes found in actual document for node ${act.getNodeName}")
    }

    StringValueComparator.compare(exp.getNodeValue, act.getNodeValue)

    if (exp.hasChildNodes){
      if (!act.hasChildNodes) throw new ComparisonError("Child nodes not found")

      compareChildNodes(exp.getChildNodes,act.getChildNodes)
    } else {
      if (act.hasChildNodes) throw new ComparisonError("Additional child nodes found")
    }
  }

  def compareChildNodes(exp:NodeList,act:NodeList)={
    if (exp.getLength != act.getLength) {
      throw new ComparisonError(s"Child nodes length not equals.")
    }

    for (idx <- 0 until exp.getLength; item = exp.item(idx)) {
      compare(item, act.item(idx))
    }
  }

  def compareAttributes(exp: NamedNodeMap, act: NamedNodeMap) = {
    val e = asList(exp)
    val a = asList(act)
    if (e.length != a.length) {
      var props = e.map(_._1).toSet -- a.map(_._1)
      if (props.nonEmpty){
        throw new ComparisonError(s"Attributes length not match. Missing property [$props]")
      } else {
        props = a.map(_._1).toSet -- e.map(_._1)
        throw new ComparisonError(s"Attributes length not match. Found unexpected property [$props]")
      }
    }

    e.foreach { e =>
      a.find(_._1 == e._1) match {
        case Some(actual) => StringValueComparator.compare(e._2, actual._2)
        case None => throw ComparisonError(s"Attribute with name ${e._1} not found")
      }
    }
  }

  def asList(o: NamedNodeMap): Seq[(String, String)] = {
    for (idx <- 0 until o.getLength; item = o.item(idx))
    yield (item.getNodeName, item.getNodeValue)
  }.toSeq
}

