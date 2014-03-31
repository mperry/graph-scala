package com.github.mperry.graph.json

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import argonaut.Parse
import com.github.mperry.graph.json.JsonHelper.JsonWeightMap


import argonaut._, Argonaut._
import scalaz._, Scalaz._


/**
 * Created by MarkPerry on 30/03/2014.
 */

@RunWith(classOf[JUnitRunner])
class JsonHelperTest extends FunSuite {

	test("parseWeights") {
		val json =
			"""
			  { "B": 100, "C": 30 }
			"""
		val o = Parse.parseOption(json)
		val z = o.flatMap(j => JsonHelper.parseEdgeWeights(j))
//		println(z)
		assert(z == Some(expectedWeightMap))
	}

	def expectedWeightMap: JsonWeightMap = {
		Map("B" -> 100, "C" -> 30)
	}

	test("parseNode") {
		val json =
			"""
			   {"A": { "B": 100, "C": 30 }}
			"""
		val o = Parse.parseOption(json)
		val z = o.flatMap(j => JsonHelper.parseNode(j))
		//		println(z)
		assert(z == Some(("A", expectedWeightMap)))
	}

	def input: String = {
		"""
			{
				"map": [
					{"A": { "B": 100, "C": 30 }},
					{"B": { "F": 300}},
					{"C": { "D": 200}},
					{"D": { "H": 90, "E":80}},
					{"E": { "H": 30, "G":150, "F":50}},
					{"F": { "G":70}},
					{"G": { "H":50}}
				]
			}
		"""
	}

	test("parseMap") {
		val og = JsonHelper.parseJson(input)
		println(og)
//		parse(input)
	}

	def parse(json: String) = {
		val og = json.decodeOption[JsonGraph]
//		println(og)
		val z = og.flatMap(g => g.toListNodeTuple)
		println(z)
		val y = og.flatMap(_.toSimpleGraph)
		println(y)

	}

}
