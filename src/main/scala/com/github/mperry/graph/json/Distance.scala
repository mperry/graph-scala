package com.github.mperry.graph.json

import argonaut._, Argonaut._
import scalaz._, Scalaz._

/**
 * Created by MarkPerry on 31/03/2014.
 */
/**
 * Json representation of distance
 * @param distance
 */
case class Distance(distance: Int) {

	// Workaround: I had trouble getting the implicit DistanceEncodeJson to work,
	// so created toJson to workaround the issue
	def toJson: Json = {
		jSingleObject(Distance.fieldName, jNumber(distance))
	}
}

object Distance {

	val fieldName = "distance"

	implicit def DistanceCodecJson: CodecJson[Distance] =
		casecodec1(Distance.apply, Distance.unapply)(fieldName)

	implicit def DistanceEncodeJson: EncodeJson[Distance] =
		EncodeJson((d: Distance) => (fieldName := d.distance) ->: jEmptyObject)

}
