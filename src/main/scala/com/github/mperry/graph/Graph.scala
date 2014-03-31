package com.github.mperry.graph

import com.github.mperry.graph.json.JsonHelper.JsonNodeMap
import PathMap._

object Graph {

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
		Graph(edgeMap(t._2))
	}

	def edgeMap(edges: Set[Edge]): EdgeMap = {
		val empty = Map.empty[Node, Map[Node, Weight]]
		edges.foldLeft(empty)((m, e) => {
			addBothToEdgeMap(m, e.from, e.to, e.distance)
//			addToEdgeMap(addToEdgeMap(m, e.from, e.to, e.distance), e.to, e.from, e.distance)
		})
	}

	def addBothToEdgeMap(m: EdgeMap, n1: Node, n2: Node, w: Weight): EdgeMap = {
		addToEdgeMap(addToEdgeMap(m, n1, n2, w), n2, n1, w)

	}

	def addToEdgeMap(m: EdgeMap, n1: Node, n2: Node, w: Weight): EdgeMap = {
		val e = createEdge(n1, n2, w)
		val t = (n1, Map(n2 -> w))
		val defaultMap: EdgeMap = m + t
		m.get(n1).map(m2 => {
			m + ((n1, m2 + ((n2, w))))
		}).getOrElse(defaultMap)
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

	def nextNode(current: Node, from: Node, to: Node): Node = {
		if (from == current) to else from
	}

	/**
	 * Returns a tuple of the path map and whether this new route is shorter and
	 * should be followed further
	 */
	def computePath(map: PathMap, current: Node, w: Weight, next: Node): (PathMap, Boolean) = {
		val previousDistance = PathMap.distance(map, next)
		val tuple = for {
			distanceThisWay <- PathMap.distance(map, current).map(_ + w)
			newList <- map.get(current).map(createEdge(current, next, w) :: _)
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

	/**
	 * Note that this is recursive (but not tail recursive) and will stack
	 * overflow for very large graphs
	 */
	def shortestPath(from: Node, current: Node, to: Node, edgeMap: EdgeMap, distances: PathMap): PathMap = {
		if (current == to) {
			distances
		} else {
			val nextEdgesMap = edgeMap.get(current)
			val newMap = nextEdgesMap.map(_.foldLeft(distances)((pathMap, kv) => {
				kv match {
					case (n, w) => {
						val next = nextNode(current, current, n)
						val t = computePath(pathMap, current, w, next)
						t match {
							case (map, b) => if (!b) map else shortestPath(from, next, to, edgeMap, map)
						}
					}
				}
			}))
			newMap.getOrElse(distances)
		}
	}

	def removeBoth(g: Graph, n1: Node, n2: Node): Graph = {
		remove(remove(g, n1, n2), n2, n1)
	}

	def remove(g: Graph, n1: Node, n2: Node): Graph = {
		val em = g.edges
		val om = em.get(n1)
		val e = om.map(m => {
			val subMap = m.-(n2)
			em + ((n1, subMap))
		}).getOrElse(g.edges)
		Graph(e)
	}

	/**
	 * Example: """ {"A": { "B": 100, "C": 30 }} """
	 */
	def mod(g: Graph, nt: JsonNodeMap): Graph = {
		nt match {
			case (n1, m) =>
				val node1 = Node(n1)
				m.foldLeft(g)((acc, kv) => {
					kv match {
						case (n2, d) => {
							val node2 = Node(n2)
							  val g2 = removeBoth(acc, node1, node2)
							val g3 = Graph(addBothToEdgeMap(g2.edges, node1, node2, d))
							g3
						}
					}
				})

		}
	}

}

case class Graph(
//					nodes: Set[Node],
					edges: EdgeMap) {

	import Graph._

	def shortestPath(from: NodeId, to: NodeId): PathMap = {
		shortestPath(Node(from), Node(to))
	}

	def shortestPath(from: Node, to: Node): PathMap = {
		shortestPath(from, from, to, Map(from -> List()))
	}

	def shortestPath(from: Node, current: Node, to: Node, distances: PathMap): PathMap = {
		Graph.shortestPath(from, current, to, edges, distances)
	}

}
