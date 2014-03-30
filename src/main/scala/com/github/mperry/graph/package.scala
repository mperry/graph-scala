package com.github.mperry

/**
 * Created by MarkPerry on 29/03/2014.
 */
package object graph {

  type Weight = Int
  val MaxWeight = Int.MaxValue
  val MinWeight = Int.MinValue

  type DistanceMap = Map[Node, List[Edge]]
	type SimpleGraph = Map[String, List[(String, Weight)]]
	type NodeName = String


}
