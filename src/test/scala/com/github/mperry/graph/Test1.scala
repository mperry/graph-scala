package com.github.mperry.graph

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

import Graph._

/**
 * Created by MarkPerry on 30/03/2014.
 */

@RunWith(classOf[JUnitRunner])
class Test1 extends FunSuite {

	def simpleGraph: SimpleGraph = {
		Map(
			"A" -> List(("B", 100)),
			"B" -> List(("C", 100))
		)
	}

	def standardGraph: SimpleGraph = {
		Map(
			"A" -> List(("B", 100), ("C", 30)),
			"B" -> List(("F", 300)),
			"C" -> List(("D", 200)),
			"D" -> List(("H", 90), ("E", 80)),
			"E" -> List(("H", 30), ("G", 150), ("F", 50)),
			"F" -> List(("G", 70)),
			"G" -> List(("H", 50))
		)
	}

	def checkDistance(d: PathMap, id: NodeId, w: Weight): Boolean = {
		distance(d, id).map(_ == w).getOrElse(false)
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

}
