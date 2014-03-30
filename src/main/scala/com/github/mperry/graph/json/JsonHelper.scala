package com.github.mperry.graph.json

import argonaut.Json
import com.github.mperry.graph.NodeId

//import StringUtils._
//import StringUtils._

/**
 * Created by MarkPerry on 30/03/2014.
 */
object JsonHelper {

	// this should be handled by an implicit conversion, but can't find the right one right now
	def parseInt(s: String): Option[Int] = {
		try {
			Some(s.toInt)
		} catch {
			case e: Exception => None
		}

	}

	type WeightMap = Map[String, Int]
	type NodeMap = Map[String, WeightMap]

	/**
	 * Example: parse(""" {"A": { "B": 100, "C": 30 }} """)
	 * @param json
	 * @return
	 */
	def parseNode(json: Json): Option[NodeMap] = {
		json.assoc.flatMap(list => {

			val m = Some(Map.empty[String, WeightMap]): Option[Map[String, WeightMap]]
			list.foldLeft(m)((acc, t) => {
				t match {
					case (f, j) => {
						acc.flatMap(m => {
							val owm = parseEdgeWeights(j)
							owm.map(wm => {
								m + ((f, wm))
							})

						})
					}
				}
			})

		})
	}

	/**
	 * Example: parse("""{ "B": 100, "C": 30 }""")
	 * @param json
	 * @return
	 */
	def parseEdgeWeights(json: Json): Option[WeightMap] = {
		json.assoc.flatMap(list => {
			val optMap = Some(Map.empty[String, Int]): Option[Map[String, Int]]
			list.foldLeft(optMap) ((acc, t) => {
				acc.flatMap(m => {
					t match {
						case (f, v) => {
							v.number.map(i => m + ((f, i.toInt)))
						}
					}
				})
			})
		})

	}

}
