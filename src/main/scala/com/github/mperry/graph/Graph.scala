package com.github.mperry.graph

case class Graph(edges: Map[Node, List[Edge]]) {


  def nextNode(current: Node, edge: Edge): Node = {
    if (edge.from == current) edge.to else edge.from
  }

  def compute(node: Node, oldDist: Weight, newDist: Weight, distances: DistanceMap, list: List[Edge]) = {
    if (oldDist <= newDist) {
      distances
    } else {
      distances.+((node, list))
    }
  }

  def shortestPath(from: Node, current: Node, to: Node, distances: DistanceMap): DistanceMap = {
    // lookup paths from this node
    val nextEdges = edges.get(current)
    val newMap = nextEdges.map(_.foldLeft(distances)((dm, e) => {
      val nn = nextNode(current, e)

      val dm2 = newDistanceMap(dm, current, e, nn)
      shortestPath(from, nn, to, dm2)
    }))
	newMap.getOrElse(distances)
  }

  def newDistanceMap(dm: DistanceMap, current: Node, e: Edge, nn: Node): DistanceMap = {
    val dm2 = for {
      distanceThisWay <- distance(dm, current).map(_ + e.distance)
      oldWay <- distance(dm, nn)
      newList <- dm.get(current).map(e::_)
    } yield (compute(nn, oldWay, distanceThisWay, dm, newList))
	dm2.getOrElse(dm)
  }

  def distance(list: List[Edge]): Weight = {
    list.foldLeft(0)((d, e) => d + e.distance)
  }

  def distance(map: DistanceMap, node: Node): Option[Weight] = {
    map.get(node).map(distance(_))
  }

}
