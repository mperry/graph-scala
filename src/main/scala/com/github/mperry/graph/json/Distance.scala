package com.github.mperry.graph.json


import argonaut._, Argonaut._
import scalaz._, Scalaz._


/**
 * Created by MarkPerry on 31/03/2014.
 */
case class Distance(distance: Int) {

	def toJson: Json = {
		jSingleObject("distance", jNumber(distance))
	}
}

object Distance {

	implicit def DistanceCodecJson: CodecJson[Distance] =
		casecodec1(Distance.apply, Distance.unapply)("distance")

	implicit def DistanceEncodeJson: EncodeJson[Distance] =
		EncodeJson((d: Distance) => ("distance" := d.distance) ->: jEmptyObject)

}
