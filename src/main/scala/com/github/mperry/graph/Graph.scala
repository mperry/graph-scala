package com.github.mperry.graph

object Graph {

	def distance(m: PathMap, n: NodeId): Option[Weight] = {
		m.get(Node(n)).map(distance(_))
	}

	def process(g: SimpleGraph): (Set[Node], Set[Edge]) = {
		val t = (Set.empty[Node], Set.empty[Edge])
		g.foldLeft(t)((acc, kv) => {
			acc match {
				case (nodes, edges) => {
					def name1 = kv._1
					def list = kv._2
					val n1 = Node(name1)
					list.foldLeft((nodes + n1, edges)) {
						case ((nodes2, edges2), (name2, w)) =>
							val n2 = Node(name2)
							val e = createEdge(n1, n2, w)
							(nodes2 + n2, edges2 + e)
					}
				}
			}
		})
	}

	def toGraph(g: SimpleGraph): Graph = {
		val t = process(g)
		Graph(t._1, edgeMap(t._2))
	}

	def edgeMap(edges: Set[Edge]): EdgeMap = {
		edges.foldLeft(Map.empty[Node, List[Edge]])((m, e) => {
			edgeMap(edgeMap(m, e.from, e), e.to, e)
		})
	}

	def edgeMap(m: EdgeMap, n: Node, e: Edge): EdgeMap = {
		val o = m.get(n)
		val edgeList = o.map(list => {
			e :: list
		}).getOrElse(List(e))
		m + ((n, edgeList))
	}

	def createEdge(x: NodeId, y: NodeId, w: Weight): Edge = {
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

	def nextNode(current: Node, edge: Edge): Node = {
		if (edge.from == current) edge.to else edge.from
	}



}

case class Graph(nodes: Set[Node], edges: EdgeMap) {

	import Graph._


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
		if (current == to) {
			distances
		} else {
			val nextEdges = edges.get(current)
			val newMap = nextEdges.map(_.foldLeft(distances)((pathMap, e) => {
				val next = nextNode(current, e)
				val t = computePath(pathMap, current, e, next)
				t match {
					case (map, b) => if (!b) map else shortestPath(from, next, to, map)
				}
			}))
			newMap.getOrElse(distances)
		}
	}

	/**
	 * Returns a tuple of the path map and whether this new route is shorter and
	 * should be followed further
	 */
	def computePath(map: PathMap, current: Node, e: Edge, next: Node): (PathMap, Boolean) = {
		val previousDistance = distance(map, next) 
		val tuple = for {
			distanceThisWay <- distance(map, current).map(_ + e.distance)
			newList <- map.get(current).map(e :: _)
		} yield (computePath(next, previousDistance, distanceThisWay, map, newList))
		tuple.getOrElse((map, true))
	}


	/**
	 * Returns the PathMap of distances and whether the new path is shorter and thus
	 * should be followed further.  This prevents endless cycles between nodes.
	 */
	def computePath(node: Node, oldDist: Option[Weight], newDist: Weight, distances: PathMap, list: List[Edge]): (PathMap, Boolean) = {
		oldDist.map(x => {
			if (x <= newDist) {
				(distances, false)
			} else {
				(distances + ((node, list)), true)
			}
		}).getOrElse((distances + ((node, list)), true))
	}

}
