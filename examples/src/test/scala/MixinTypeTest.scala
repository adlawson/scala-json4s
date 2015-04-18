package com.adlawson.json4s

import org.scalatest.{FlatSpec, Matchers}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

class MixinTypeTest extends FlatSpec with Matchers {

  import MixinType._

  implicit val formats = DefaultFormats + FooBarSerializer

  "read" must "deserialize json and extract it into FooBar" in {
    val fbar = read[FooBar]("""{"a":1,"b":2}""")

    fbar shouldBe a[FooBar]
    fbar.a shouldBe(1)
    fbar.b shouldBe(2)
  }

  "write" must "serialize FooBar into a valid json string" in {
    val json = write(new Foo(1) with Bar { val b = 2 })
    val done = read[Map[String, Int]](json)

    done shouldBe(Map("a"->1, "b"->2))
  }

}
