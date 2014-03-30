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
	test("someLibraryMethod is always true") {
		assert(true)
	}

	def simpleMap: SimpleGraph = {
		Map(
			"A" -> List(("B", 100)),
			"B" -> List(("C", 100))
		)
	}

	def fullMap: SimpleGraph = {
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

	test("noSteps") {
		val g = Graph.toGraph(simpleMap)
		val n = "A"
		val d = g.shortestPath(n, n)

		assert(d.get(Node(n)).map(_.size == 0).getOrElse(false))
//		println(d)
	}

	test("oneStep") {
		val g = Graph.toGraph(simpleMap)
		val d = g.shortestPath("A", "B")
		def b = distance(d, "B").map(_ == 100).getOrElse(false)
		assert(b)
//		println(d)
	}

	test("twoSteps") {
		val g = Graph.toGraph(simpleMap)
		val d = g.shortestPath("A", "C")
		val b = distance(d, "C").map(_ == 200).getOrElse(false)
		assert(b)

		println(d)
	}

	test("test1") {
//		def m = simpleMap
		def m = fullMap
		val g = Graph.toGraph(m)
//		val dist = g.shortestPath(Node("A"), Node("A"))
//		println(dist)
		val d2 = g.shortestPath(Node("A"), Node("D"))
		println(d2)

	}

}
