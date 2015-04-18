package com.adlawson.json4s

import org.scalatest.{FlatSpec, Matchers}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

class EnumTypeTest extends FlatSpec with Matchers {

  import EnumType._

  case class Foo(a: Int, dir: Direction)

  implicit val formats = DefaultFormats + DirectionSerializer

  "read" must "deserialize json and extract it into the case class with Direction" in {
    val foo = read[Foo](s"""{"a":1,"dir":"${Direction.Left.value}"}""")

    foo.a shouldBe(1)
    foo.dir shouldBe(Direction.Left)
  }

  "write" must "serialize the case class with Direction into a valid json string" in {
    val json = write(Foo(1, Direction.Right))
    val done = read[Map[String, _]](json)

    done shouldBe(Map("a"->1, "dir"->Direction.Right.value))
  }

}
