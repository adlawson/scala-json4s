package com.adlawson.json4s

import org.scalatest.{FlatSpec, Matchers}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

class CharSerializerTest extends FlatSpec with Matchers {

  case class Foo(a: Char, b: String)

  implicit val formats = DefaultFormats + CharSerializer

  "read" must "deserialize json and extract it into the case class" in {
    val foo = read[Foo]("""{"a":"a","b":"b"}""")

    foo.a shouldBe('a')
    foo.b shouldBe("b")
  }

  "write" must "serialize the case class into a valid json string" in {
    val raw = write(Foo('a', "b"))
    val foo = read[Foo](raw)

    foo.a shouldBe('a')
    foo.b shouldBe("b")
  }

}
