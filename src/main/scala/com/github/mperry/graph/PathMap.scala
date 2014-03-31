package com.github.mperry.graph

/**
 * Created by MarkPerry on 31/03/2014.
 */

object PathMap {

	def distance(m: PathMap, n: NodeId): Option[Weight] = {
		m.get(Node(n)).map(Graph.distance(_))
	}

	def distance(map: PathMap, node: Node): Option[Weight] = {
		map.get(node).map(Graph.distance(_))
	}

}
