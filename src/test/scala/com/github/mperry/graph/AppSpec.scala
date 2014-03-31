package com.github.mperry.graph

import org.scalacheck._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

/**
 * Created by MarkPerry on 1/04/2014.
 */
object AppSpec extends Properties("AppSpec") {
	include(StringSpec)
}

@RunWith(classOf[JUnitRunner])
class AppSpec extends FunSuite {

	test("app suite") {
		AppSpec.check
	}

}
