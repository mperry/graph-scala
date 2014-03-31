package com.github.mperry.graph

import scala.io.Source
import com.github.mperry.graph.json.{Distance, Search, JsonHelper}
import scala.annotation.tailrec
import argonaut._, Argonaut._

object Repl {

	val quitText = "q"

	def main(args: Array[String]) = {
		if (args.length < 1) {
			println("No command line argument for input file found.")
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
		val og2 = og.map(g => {
			def g2 = Graph.toGraph(g)
			println(s"graph: $g2")
			repl(g2)
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
		println("Enter q to quit or json text")
		step(g)
	}

	def isQuit(s: String): Boolean = {
		s.trim() == quitText
	}

	def processSearch(g: Graph, s: Search): Graph = {
		def m = g.shortestPath(s.start, s.end)
		val optDist = m.get(Node(s.end)).map(d => Distance(Graph.distance(d)))
		val default = jSingleObject("error", jString("some error"))
		val json = optDist.map(d => d.toJson).getOrElse(default)
		println(json)
		g
	}

	def processSearch(g: Graph, text: String): Option[Graph] = {
		Search.parse(text).map(processSearch(g, _))
	}

	def processMod(g: Graph, text: String): Option[Graph] = {
		val o = Parse.parseOption(text).flatMap(JsonHelper.parseNode(_))
		o.map(nt => Graph.mod(g, nt))
	}

	def processLine(g: Graph, text: String): Graph = {
		println(s"You entered $text")
		val o2 = processSearch(g, text)
		val o3 = processMod(g, text)
		println(s"search: $o2 mod: $o3")
		o2.orElse(o3).getOrElse(g)
	}

}