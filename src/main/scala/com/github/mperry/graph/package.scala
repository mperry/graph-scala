package com.github.mperry

/**
 * Created by MarkPerry on 29/03/2014.
 */
package object graph {

	type Weight = Int
	type PathMap = Map[Node, List[Edge]]
//	type EdgeMap = Map[Node, List[Edge]]
	type EdgeMap = Map[Node, Map[Node, Edge]]
	type SimpleGraph = Map[NodeId, Map[NodeId, Weight]]
	type NodeId = String

}
