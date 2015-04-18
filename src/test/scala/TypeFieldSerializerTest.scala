package com.adlawson.json4s

import org.scalatest.{FlatSpec, Matchers}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

class TypeFieldSerializerTest extends FlatSpec with Matchers {

  case class Foo(a: Int, typ: Int)

  implicit val formats = DefaultFormats + TypeFieldSerializer

  "read" must "deserialize json with `type` field and extract it into a class with `typ`" in {
    val fbar = read[Foo]("""{"a":1,"type":2}""")

    fbar.a shouldBe(1)
    fbar.typ shouldBe(2)
  }

  "write" must "serialize the class with `typ` into a valid json string with `type` field" in {
    val json = write(Foo(1, 2))

    json should include ("type")
  }

}
