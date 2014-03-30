package com.github.mperry.graph

import scala.io.Source

object Solution {

	def main(args: Array[String]) = {
		println("Hi from scala2")
		val n = args.length
		println(s"Scala command args: $args size: $n")
		args.foreach(s => println(s"arg: $s"))
		val filename = args(0)
		println(s"file: $filename")
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
	}

}
