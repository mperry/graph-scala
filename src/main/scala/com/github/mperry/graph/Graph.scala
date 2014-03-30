package com.github.mperry.graph

object Graph {

	def distance(m: PathMap, n: NodeId): Option[Weight] = {
		m.get(Node(n)).map(distance(_))
	}

	def process(m: Map[String, List[(String, Weight)]]): (Set[Node], Set[Edge]) = {

		def nodes = Set.empty[Node]
		def edges = Set.empty[Edge]


		m.foldLeft((nodes, edges))((acc, kv) => {
			acc match {
				case (ns, es) => {
					def name1 = kv._1
					def list = kv._2
					val n1 = Node(name1)
					list.foldLeft((ns + n1, es)) {
						case ((nodes2, edges2), (name2, w)) =>
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

	def edgeMap(edges: Set[Edge]): EdgeMap = {
		edges.foldLeft(Map.empty[Node, List[Edge]])((m, e) => {
			edgeMap(edgeMap(m, e.from, e), e.to, e)
		})
	}

	def edgeMap(m: Map[Node, List[Edge]], n: Node, e: Edge): PathMap = {

		// deal with from first
		val o = m.get(n)
		val edgeList = o.map(list => {
			e :: list
		}).getOrElse(List(e))

		m + ((n, edgeList))
	}

	def createEdge(x: String, y: String, w: Weight): Edge = {
		createEdge(Node(x), Node(y), w)
	}


	def createEdge(x: Node, y: Node, w: Weight): Edge = {
		val t = if (x.name < y.name) (x, y) else (y, x)
		Edge(t._1, t._2, w)
	}


	def distance(list: List[Edge]): Weight = {
		list.foldLeft(0)((d, e) => d + e.distance)
	}

	def distance(map: PathMap, node: Node): Option[Weight] = {
		map.get(node).map(distance(_))
	}

}

case class Graph(nodes: Set[Node], edges: EdgeMap) {

	import Graph._

	def nextNode(current: Node, edge: Edge): Node = {
		if (edge.from == current) edge.to else edge.from
	}

	def compute(node: Node, oldDist: Option[Weight], newDist: Weight, distances: PathMap, list: List[Edge]): (PathMap, Boolean) = {
		oldDist.map(x => {
			if (x <= newDist) {
				(distances, false)
			} else {
				(distances.+((node, list)), true)
			}
		}).getOrElse((distances + ((node, list)), true))

	}

	def shortestPath(from: NodeId, to: NodeId): PathMap = {
		shortestPath(Node(from), Node(to))
	}

	def shortestPath(from: Node, to: Node): PathMap = {
		shortestPath(from, from, to, Map(from -> List()))
	}

	/**
	 * Note that this is recursive (but not tail recursive) and will stack
	 * overflow for very large graphs
	 */
	def shortestPath(from: Node, current: Node, to: Node, distances: PathMap): PathMap = {
		// lookup paths from this node
		if (current == to) {
			distances
		} else {
			val nextEdges = edges.get(current)
			val newMap = nextEdges.map(_.foldLeft(distances)((dm, e) => {
				val nn = nextNode(current, e)
				val t = newDistanceMap(dm, current, e, nn)
				t match {
					case (dm2, b) => if (!b) dm2 else shortestPath(from, nn, to, dm2)
				}

			}))
			newMap.getOrElse(distances)
		}

	}

	def newDistanceMap(dm: PathMap, current: Node, e: Edge, nn: Node): (PathMap, Boolean) = {
		val o1 = Graph.distance(dm, current).map(_ + e.distance)
		val oldWay = distance(dm, nn)
		val o3 = dm.get(current).map(e :: _)
		val dm2 = for {
			distanceThisWay <- o1
			//      oldWay <- o2
			newList <- o3
		} yield (compute(nn, oldWay, distanceThisWay, dm, newList))
		dm2.getOrElse((dm, true))
	}

}
