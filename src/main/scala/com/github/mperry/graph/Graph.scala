package com.github.mperry.graph

object Graph {

	def process(m: Map[String, List[(String, Weight)]]): (Set[Node], Set[Edge]) = {

		def nodes = Set.empty[Node]
		def edges = Set.empty[Edge]


		m.foldLeft((nodes, edges))((acc, kv) => {
			acc match {
				case (ns, es) => {
					def name1 = kv._1
					def list = kv._2
					val n1 = Node(name1)
					list.foldLeft((ns + n1, es)) { case ((nodes2, edges2), (name2, w)) =>
						val n2 = Node(name2)
						val e = createEdge(n1, n2, w)
						(nodes2 + n2, edges2 + e)
					}
				}
			}

		})
//		(nodes, edges)
	}

	def toGraph(g: SimpleGraph): Graph = {

		val t = process(g)
//		val m = edgeMap(t._2)
		Graph(t._1, edgeMap(t._2))

	}

	def edgeMap(edges: Set[Edge]): Map[Node, List[Edge]] = {
		edges.foldLeft(Map.empty[Node, List[Edge]])((m, e) => {
			edgeMap(edgeMap(m, e.from, e), e.to, e)
		})
	}
	
	def edgeMap(m: Map[Node, List[Edge]], n: Node, e: Edge): DistanceMap = {

		// deal with from first
		val o = m.get(n)
		val edgeList = o.map(list => {
			e::list
		}).getOrElse(List(e))

		m + ((n, edgeList))
	}

	def createEdge(x: String, y: String, w: Weight): (Edge) = {
		createEdge(Node(x), Node(y), w)
	}


	def createEdge(x: Node, y: Node, w: Weight): (Edge) = {
		val t = if (x.name < y.name) (x, y) else (y, x)
		Edge(t._1, t._2, w)
	}


	def distance(list: List[Edge]): Weight = {
		list.foldLeft(0)((d, e) => d + e.distance)
	}

	def distance(map: DistanceMap, node: Node): Option[Weight] = {
		map.get(node).map(distance(_))
	}

}

case class Graph(nodes: Set[Node], edges: Map[Node, List[Edge]]) {

	import Graph._

  def nextNode(current: Node, edge: Edge): Node = {
    if (edge.from == current) edge.to else edge.from
  }

  def compute(node: Node, oldDist: Option[Weight], newDist: Weight, distances: DistanceMap, list: List[Edge]) = {
	oldDist.map(x => {
		if (x <= newDist) {
			distances
		} else {
			distances.+((node, list))
		}
	}).getOrElse(distances + ((node, list)))

  }

	def shortestPath(from: NodeName, to: NodeName): DistanceMap = {
		shortestPath(Node(from), Node(to))
	}

	def shortestPath(from: Node, to: Node): DistanceMap = {
		shortestPath(from, from, to, Map(from -> List()))
	}

  def shortestPath(from: Node, current: Node, to: Node, distances: DistanceMap): DistanceMap = {
    // lookup paths from this node
	if (current == to) {
		distances
	} else {
		val nextEdges = edges.get(current)
		val newMap = nextEdges.map(_.foldLeft(distances)((dm, e) => {
			val nn = nextNode(current, e)
			val dm2 = newDistanceMap(dm, current, e, nn)
			shortestPath(from, nn, to, dm2)
		}))
		newMap.getOrElse(distances)
	}

  }

  def newDistanceMap(dm: DistanceMap, current: Node, e: Edge, nn: Node): DistanceMap = {
	val o1 = Graph.distance(dm, current).map(_ + e.distance)
	val oldWay = distance(dm, nn)
	val o3 = dm.get(current).map(e::_)
    val dm2 = for {
      distanceThisWay <- o1
//      oldWay <- o2
      newList <- o3
    } yield (compute(nn, oldWay, distanceThisWay, dm, newList))
	dm2.getOrElse(dm)
  }

}
