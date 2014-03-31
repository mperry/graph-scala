package com.github.mperry.graph

/**
 * Created by MarkPerry on 31/03/2014.
 */
import org.scalacheck._

object StringSpec extends Properties("StringSpec") {
	import Prop.forAll

	// write a simple property so I know this is integrated with the unit tests
	property("startsWith") = forAll { (a: String, b: String) =>
		(a+b).startsWith(a)
	}

}