package com.adlawson.json4s

import org.scalatest.{FlatSpec, Matchers}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

class TypeSerializerTest extends FlatSpec with Matchers {

  import TypeSerializerTest._

  implicit val formats = DefaultFormats + FooBarSerializer

  "read" must "deserialize json and extract it into the Type Alias" in {
    val fbar = read[FooBar]("""{"a":1,"b":2}""")

    fbar shouldBe a[FooBar]
    fbar.a shouldBe(1)
    fbar.b shouldBe(2)
  }

  "write" must "serialize the Type Alias into a valid json string" in {
    val json = write(new Foo(1) with Bar { val b = 2 })
    val done = read[Map[String, Int]](json)

    done shouldBe(Map("a"->1, "b"->2))
  }

}

object TypeSerializerTest {

  type FooBar = Foo with Bar

  case class Foo(a: Int)
  trait Bar { def b: Int }

  private[json4s] case class _FooBar(a: Int, b: Int)

  object FooBarSerializer extends TypeSerializer[FooBar, _FooBar] {
    override def wrap(f: FooBar) = _FooBar(f.a, f.b)
    override def unwrap(f: _FooBar) = new Foo(f.a) with Bar { val b = f.b }
  }

}
