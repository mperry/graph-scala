package com.github.mperry.graph.json

import argonaut.Json
import com.github.mperry.graph.{SimpleGraph, NodeId}
import argonaut._, Argonaut._
import scalaz._, Scalaz._

/**
 * Created by MarkPerry on 30/03/2014.
 */
object JsonHelper {

	// WeightMap represents json """{ "B": 100, "C": 30 }"""
	type WeightMap = Map[String, Int]
	// NodeTuple represents json """ {"A": { "B": 100, "C": 30 }} """
	type NodeTuple = (String, WeightMap)

	def parseJson(json: String): Option[SimpleGraph] = {
		val og = json.decodeOption[JsonGraph]
		og.flatMap(_.toSimpleGraph)
	}

	// this should be handled by an implicit conversion, but can't find the right one right now
	def parseInt(s: String): Option[Int] = {
		try {
			Some(s.toInt)
		} catch {
			case e: Exception => None
		}
	}

	/**
	 * Example: parse(""" {"A": { "B": 100, "C": 30 }} """)
	 * @param json
	 * @return
	 */
	def parseNode(json: Json): Option[NodeTuple] = {
		json.assoc.flatMap(list => {
			list.head match {
				case (f, j) => {
					val owm = parseEdgeWeights(j)
					owm.map((f, _))
				}
			}
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
