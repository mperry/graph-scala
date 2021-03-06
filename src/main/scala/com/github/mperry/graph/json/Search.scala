package com.github.mperry.graph.json

import argonaut._, Argonaut._
import scalaz._, Scalaz._

/**
 * Created by MarkPerry on 31/03/2014.
 */
case class Search(start: String, end: String) {
}

object Search {

	val startKey = "start"
	val endKey = "end"

	implicit def SearchCodecJson: CodecJson[Search] =
		casecodec2(Search.apply, Search.unapply)(startKey, endKey)

	def parseSearch(json: String): Option[Search] = {
		json.decodeOption[Search]
	}

}

