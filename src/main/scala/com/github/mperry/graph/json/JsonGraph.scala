package com.github.mperry.graph.json

import argonaut._, Argonaut._
import scalaz._, Scalaz._
import com.github.mperry.graph.json.JsonHelper.NodeMap

/**
 * Created by MarkPerry on 30/03/2014.
 */
case class JsonGraph(map: List[Json]) {

	def toListNodeMap: Option[List[NodeMap]] = {
		val a = Some(Nil): Option[List[NodeMap]]
		map.foldLeft(a)((acc, json) => {
			val onm = JsonHelper.parseNode(json)
			acc.flatMap(list => onm.map(nm => nm::list))
		})
	}
}

object JsonGraph {
	implicit def JsonGraphCodecJson: CodecJson[JsonGraph] =
		casecodec1(JsonGraph.apply, JsonGraph.unapply)("map")

}