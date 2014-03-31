package com.github.mperry.graph.json

/**
 * Created by MarkPerry on 1/04/2014.
 */

import org.scalacheck._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import argonaut.Json

object DistanceSpec extends Properties("DistanceSpec") {

	import Prop.forAll

	property("distanceRoundTrip") = forAll {
		(i: Int) =>
			roundTrip(i)
	}

	def toJson(i: Int): Json = {
		Distance(i).toJson
	}

	def fromJson(json: Json): Option[String] = {
		val o = json.obj
		val field = o.flatMap(_.toMap.get(Distance.fieldName))
		val result = field.flatMap(_.number).map(_.toInt.toString)
		result
	}

	def roundTrip(i: Int): Boolean = {
		val o1 = fromJson(toJson(i))
		val o2 = Some(i.toString)
		o1 == o2
	}

}

@RunWith(classOf[JUnitRunner])
class DistanceSpec extends FunSuite {

	test("test1") {
		val b = DistanceSpec.roundTrip(2)
//		println(b)
		assert(b)
	}

}

