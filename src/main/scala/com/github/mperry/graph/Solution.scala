package com.github.mperry.graph

import scala.io.Source
import com.github.mperry.graph.json.JsonHelper
import scala.util.control.TailCalls.TailRec
import scala.annotation.tailrec

object Solution {

	def main(args: Array[String]) = {
		val filename = args(0)
		println(s"filename: $filename")
		process(filename)
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
			processLine(line)
			step(g)
		}
	}

	def repl(g: Graph) = {
		println("Enter q to quit or json text")
		step(g)
	}

	def isQuit(s: String): Boolean = {
		s.trim() == "q"
	}

	def processLine(text: String) = {
		println(s"You entered $text")
	}

}
