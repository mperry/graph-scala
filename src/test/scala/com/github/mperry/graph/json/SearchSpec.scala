package com.github.mperry.graph.json

import org.scalacheck._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import argonaut.{Parse, Json}

/**
 * Created by MarkPerry on 1/04/2014.
 */

object SearchSpec extends Properties("SearchSpec") {

	import Prop.forAll

	property("searchRoundTrip") = forAll {
		(a: String, b: String) =>
			roundTrip(a, b)
	}

	def json(start: String, end: String): String = {
		s""" { "${Search.startKey}":"$start", "${Search.endKey}":"$end" } """
	}

	def tuple(s: String): Option[(String, String)] = {
		val om = Parse.parseOption(s).flatMap(_.obj).map(_.toMap)
		def start = om.flatMap(_.get(Search.startKey))
		def end = om.flatMap(_.get(Search.endKey))
		val result = for {
			s <- start.flatMap(_.string)
			e <- end.flatMap(_.string)
		} yield ((s, e))
		result
	}

	def roundTrip(start: String, end: String): Boolean = {
		val s = json(start, end)
		val o1 = Some((start, end))
		val o2 = tuple(s)
		o1 == o2
	}

}

@RunWith(classOf[JUnitRunner])
class SearchSpec extends FunSuite {

//	import SearchSpec._

	test("round trip") {
		val b = SearchSpec.roundTrip("a", "b")
		assert(b)
	}

}
