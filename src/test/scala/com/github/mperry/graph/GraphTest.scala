package com.github.mperry.graph

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

import Graph._
import com.github.mperry.graph.json.JsonHelper

/**
 * Created by MarkPerry on 30/03/2014.
 */

@RunWith(classOf[JUnitRunner])
class GraphTest extends FunSuite {

	def trivialGraph: SimpleGraph = {
		Map(
			"A" -> Map("B" -> 100)
		)
	}


	def simpleGraph: SimpleGraph = {
		Map(
			"A" -> Map("B" -> 100),
			"B" -> Map("C" -> 100)
		)
	}

	def standardGraph: SimpleGraph = {
		Map(
			"A" -> Map("B" -> 100, "C" -> 30),
			"B" -> Map("F" -> 300),
			"C" -> Map("D" -> 200),
			"D" -> Map("H" -> 90, "E" -> 80),
			"E" -> Map("H" -> 30, "G" -> 150, "F" -> 50),
			"F" -> Map("G" -> 70),
			"G" -> Map("H" -> 50)
		)
	}

	def checkDistance(d: PathMap, id: NodeId, w: Weight): Boolean = {
		PathMap.distance(d, id).map(_ == w).getOrElse(false)
	}

	test("no steps for simple graph") {
		val n = "A"
		val pathMap = toGraph(simpleGraph).shortestPath(n, n)
		assert(pathMap.get(Node(n)).map(_.size == 0).getOrElse(false))
	}

	test("one step for simple graph") {
		val pathMap = toGraph(simpleGraph).shortestPath("A", "B")
		assert(checkDistance(pathMap, "B", 100))
	}

	test("two steps for simple graph") {
		val pathMap = toGraph(simpleGraph).shortestPath("A", "C")
		assert(checkDistance(pathMap, "C", 200))
	}

	test("two steps for standard graph") {
		val pathMap = toGraph(standardGraph).shortestPath("A", "D")
		assert(checkDistance(pathMap, "D", 230))
	}

	test("a to f") {
		val map = toGraph(standardGraph).shortestPath("A", "F")
		assert(checkDistance(map, "F", 360))
	}

	test("add path") {
		val s = """ {"A": { "C": 30 } } """
		val expected =
			Map (
				"A" -> Map("B" -> 100, "C" -> 30)
			)
		assert(checkGraph(trivialGraph, s, expected))
	}

	test("add change path") {
		val s = """ {"A": { "B": 80, "C": 30 } } """
		val expected = Map(
			"A" -> Map("B" -> 80, "C" -> 30)
		)
		assert(checkGraph(trivialGraph, s, expected))
	}

	def checkGraph(g: SimpleGraph, json: String, expected: SimpleGraph): Boolean = {
		val o = JsonHelper.parseNodeMap(json)
		o.map(tuple => {
			Graph.mod(toGraph(g), tuple) == toGraph(expected)
		}).getOrElse(false)
	}



}
