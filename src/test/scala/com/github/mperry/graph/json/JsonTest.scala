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

	test("true") {
		assert(true)
	}


	def parse(json: String) = {
		val og = json.decodeOption[JsonGraph]
		println(og)
	}

//	def parse(json: String) = {
//
////		val r = Parse.parseOption(json)
//		//		println(r)
//		//		r.map(j => j.)
//		val optionGraph = json.decodeOption[JsonGraph]
//
//		println(optionGraph)
//		optionGraph.flatMap(g => {
//			// return option SimpleGraph
//			val jsonList = g.map
//			val emptyMap = Map.empty[NodeId, List[(NodeId, Weight)]]
//			jsonList.foldLeft(Some(emptyMap))((acc, json) => {
//				// return Option SimpleGraph
////				call()
//				json.assoc
//
//				acc.flatMap(m => {
//					// return Option SimpleGraph
//					json.assoc.flatMap(list => {
//						// transform List[(JsonField, Json)] to Option SimpleGraph
//						list.foldLeft(Some(emptyMap))((acc2, t) => {
//							// return Option SimpleGraph
//							t match {
//								case (k, j) => {
//									j.assoc.flatMap(list2 => {
//										// transfrom list2: List[JsonField, Json] to Option SimpleGraph
//										list2.foldLeft(Some(emptyMap))((acc3, t) => {
//											// return Option SimpleGraph
//											t match {
//												case (k2, j2) => {
//													j2.string.map(s => {
//														// return SimpleGraph
//														emptyMap
//													})
//												}
//											}
//										})
//
//									})
//								}
//
//							}
////							acc2
//						})
//
//					})
//
//				})
//
//			})
//
//		})
//	}

	def print(json: String) = {
		val optionGraph = json.decodeOption[JsonGraph]
		optionGraph.map(g => {
			val jsonList = g.map
			jsonList.map(json => {
				println(s"json$json")


				json.assoc.map(list => {
					// list is List[(JsonField, Json)]
					list.foreach { case (f, j) =>
						println(s"field: $f json: $j")
						j.assoc.map(list2 => {
							// list2 is List[JsonField, Json)]
							list2.foreach { case (k2, j2) =>
								println(s"key: $k2 w: $j2")
								j2.string

							}
						})

					}
				})
				//				val oList = json.objectFields
				//				oList.map(list => {
				//
				//				})
			})
		})
	}

	test("test1") {

//		parse(input)
	}

}
