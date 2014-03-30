package com.github.mperry.graph.json

import argonaut._, Argonaut._
import scalaz._, Scalaz._
import com.github.mperry.graph.json.JsonHelper.NodeTuple
import com.github.mperry.graph.{Weight, NodeId, SimpleGraph}

/**
 * Created by MarkPerry on 30/03/2014.
 */
case class JsonGraph(map: List[Json]) {

	def toListNodeTuple: Option[List[NodeTuple]] = {
		val a = Some(Nil): Option[List[NodeTuple]]
		map.foldLeft(a)((acc, json) => {
			val onm = JsonHelper.parseNode(json)
			acc.flatMap(list => onm.map(nm => nm::list))
		})
	}
	
	def toSimpleGraph: Option[SimpleGraph] = {

		// m: SimpleGraph
		val m = Map.empty[NodeId, List[(NodeId, Weight)]]
		toListNodeTuple.map(nodeTuples => {
			// return SimpleGraph
			nodeTuples.foldLeft(m)((acc, nt) => {
				// return SimpleGraph
				val id = nt._1
				val weightMap = nt._2

				weightMap.foldLeft(acc)((acc2, kv) => {
					kv match {
						case (s, i) => {
							val newList = acc2.get(id).map(list => (s, i)::list).getOrElse(List((s, i)))
							acc2 + ((id, newList))
//							m + (id, m.get(id).map(list => (s, i)::list).getOrElse(List((s, i))))
//							acc + (nt._1)

						}
					}

				})
//				m + (nt._1, nt._2)
			
			})
		})
		
		
	}
	
}

object JsonGraph {
	implicit def JsonGraphCodecJson: CodecJson[JsonGraph] =
		casecodec1(JsonGraph.apply, JsonGraph.unapply)("map")

}