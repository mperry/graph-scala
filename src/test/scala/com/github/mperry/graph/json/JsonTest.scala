package com.github.mperry.graph.json

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import argonaut._, Argonaut._

/**
 * Created by MarkPerry on 30/03/2014.
 */
object JsonTest {

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
}

@RunWith(classOf[JUnitRunner])
class JsonTest extends FunSuite {

	def parse(json: String) = {
		val og = json.decodeOption[JsonGraph]
		println(og)
	}

}
