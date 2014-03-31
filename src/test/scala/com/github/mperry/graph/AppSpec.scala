package com.github.mperry.graph

import org.scalacheck._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import com.github.mperry.graph.json.{SearchSpec, DistanceSpec}

/**
 * Created by MarkPerry on 1/04/2014.
 */
object AppSpec extends Properties("AppSpec") {
	include(StringSpec)
	include(DistanceSpec)
	include(SearchSpec)

}

@RunWith(classOf[JUnitRunner])
class AppSpec extends FunSuite {

	test("app suite") {
		AppSpec.check
	}

}
