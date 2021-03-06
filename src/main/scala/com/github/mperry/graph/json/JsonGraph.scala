package com.github.mperry.graph.json

import argonaut._, Argonaut._
import scalaz._, Scalaz._
import com.github.mperry.graph.json.JsonHelper.JsonNodeMap
import com.github.mperry.graph.{Weight, NodeId, SimpleGraph}

/**
 * Created by MarkPerry on 30/03/2014.
 */
case class JsonGraph(map: List[Json]) {

	def toListNodeTuple: Option[List[JsonNodeMap]] = {
		val a = Some(Nil): Option[List[JsonNodeMap]]
		map.foldLeft(a)((acc, json) => {
			val onm = JsonHelper.parseNodeMap(json)
			acc.flatMap(list => onm.map(nm => nm::list))
		})
	}
	
	def toSimpleGraph: Option[SimpleGraph] = {
		val m = Map.empty[NodeId, Map[NodeId, Weight]]
		toListNodeTuple.map(nodeTuples => {
			nodeTuples.foldLeft(m)((acc, nt) => {
				val id = nt._1
				val weightMap = nt._2

				weightMap.foldLeft(acc)((acc2, kv) => {
					kv match {
						case (s, i) => {
							val newMap = acc2.get(id).map(oldMap => oldMap + ((s, i))).getOrElse(Map(s -> i))
							acc2 + ((id, newMap))
						}
					}
				})
			})
		})
	}
	
}

object JsonGraph {
	implicit def JsonGraphCodecJson: CodecJson[JsonGraph] =
		casecodec1(JsonGraph.apply, JsonGraph.unapply)("map")
}
