package com.github.mperry.graph

import scala.io.Source
import com.github.mperry.graph.json.{Distance, Search, JsonHelper}
import scala.annotation.tailrec
import argonaut._, Argonaut._

object Repl {

	val quitText = "q"

	def main(args: Array[String]) = {
		if (args.length < 1) {
			println("No command line argument for input file")
		} else {
			val filename = args(0)
			println(s"filename: $filename")
			process(filename)
		}
	}

	def process(filename: String) = {
		val source = Source.fromFile(filename)
		val text = source.mkString
		source.close()
		processJson(text)
	}

	def processJson(json: String) = {
		println(s"json: $json")
		val og = JsonHelper.parseJson(json)
		val og2 = og.map(sg => {
			def g = Graph.toGraph(sg)
			println(s"graph: $g")
			repl(g)
		})
	}

	def nextLine(prompt: String): String = {
		print(prompt)
		readLine
	}

	@tailrec
	def step(g: Graph): Unit = {
		val line = nextLine("> ")
		if (!isQuit(line)) {
			step(processLine(g, line))
		}
	}

	def repl(g: Graph) = {
		println("Enter q to quit or json text to search or modify graph")
		step(g)
	}

	def isQuit(s: String): Boolean = {
		s.trim() == quitText
	}

	def processSearch(g: Graph, s: Search): Graph = {
		println(s"search: $s")
		def m = g.shortestPath(s.start, s.end)
		val optDist = m.get(Node(s.end)).map(d => Distance(Graph.distance(d)))
		val default = jSingleObject("error", jString("some error"))
		val json = optDist.map(d => d.toJson).getOrElse(default)
		println(json)
		g
	}

	def processSearch(g: Graph, text: String): Option[Graph] = {
		Search.parseSearch(text).map(processSearch(g, _))
	}

	def processMod(g: Graph, text: String): Option[Graph] = {
//		val o = Parse.parseOption(text).flatMap(JsonHelper.parseNodeMap(_))
		val o = JsonHelper.parseNodeMap(text)
		o.map(nt => {
			println(s"modify: $nt")
			def result = Graph.mod(g, nt)
			println(s"new graph: $result")
			result
		})
	}

	def processLine(g: Graph, text: String): Graph = {
//		println(s"You entered $text")
		val o2 = processSearch(g, text)
		val o3 = processMod(g, text)
//		println(s"search: $o2 mod: $o3")
		val result = o2.orElse(o3).getOrElse(g)
//		println(s"graph state: $result")
		result
	}

}
