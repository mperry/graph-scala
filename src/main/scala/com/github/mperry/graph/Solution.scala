package com.github.mperry.graph

import scala.io.Source
import com.github.mperry.graph.json.JsonHelper

object Solution {

	def main(args: Array[String]) = {
//		println("Hi from scala2")
		val n = args.length
//		println(s"Scala command args: $args size: $n")
//		args.foreach(s => println(s"arg: $s"))
		val filename = args(0)
		println(s"filename: $filename")
		process(filename)

	}

	def process(filename: String) = {
//		File(filename)
		val source = Source.fromFile(filename)
		val text = source.mkString
		source.close()
		processJson(text)
	}

	def processJson(json: String) = {
		println(s"json: $json")
		val og = JsonHelper.parseJson(json)
		val og2 = og.map(g => Graph.toGraph(g))
		println(s"graph: $og2")
		repl

	}

	def nextLine(prompt: String): String = {

		print(prompt)
		readLine
//		val lines = Source.fromInputStream(System.in).getLines()
//		println(s"found lines $lines")
//		""
	}

	def repl = {
		println("Enter q to quit or json text")

//		println("> ")

		var loop = true
		while (loop) {
			val line = nextLine("> ")
			val b = isQuit(line)
			if (isQuit(line)) {
				loop = false
			} else {
				processLine(line)
			}

		}

//		for (ln <- io.Source.stdin.getLines) processLine(ln)

	}

	def isQuit(s: String): Boolean = {
		s.trim() == "q"
	}

	def processLine(text: String) = {
		println(s"You entered $text")
	}

}
