package com.adlawson.json4s

import org.scalatest.{FlatSpec, Matchers}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

class PolymorphicTypeTest extends FlatSpec with Matchers {

  import PolymorphicType._
  import PolymorphicTypeTest._

  implicit val formats = DefaultFormats + FurnitureSerializer

  "read" must "deserialize json and extract it into a Chair record" in {
    val result = read[Chair](rawChair)

    result shouldBe a[Chair]
    result shouldBe(chair)
  }

  it must "deserialize json and extract it into a Table record" in {
    val result = read[Table](rawTable)

    result shouldBe a[Table]
    result shouldBe(table)
  }

  it must "deserialize json and extract it into a polymorphic list of Furniture records" in {
    val results = read[List[Furniture]](s"""[$rawChair,$rawTable]""")

    results.head shouldBe a[Chair]
    results.last shouldBe a[Table]
    results.head shouldBe(chair)
    results.last shouldBe(table)
  }

  "write" must "serialize a Chair record into a valid json string" in {
    val json = write(chair)
    val done = read[Map[String, _]](json)

    done shouldBe(Map(
      "legs"->chair.legs,
      "occupancy"->chair.occupancy))
  }

  it must "serialize a Table record into a valid json string" in {
    val json = write(table)
    val done = read[Map[String, _]](json)

    done shouldBe(Map(
      "legs"->table.legs,
      "capacity"->table.capacity))
  }

  it must "serialize a polymorphic list of Furniture records into a valid json string" in {
    val json = write(List(chair, table))
    val done = read[List[Map[String, _]]](json)

    done.head shouldBe(Map(
      "legs"->chair.legs,
      "occupancy"->chair.occupancy))

    done.last shouldBe(Map(
      "legs"->table.legs,
      "capacity"->table.capacity))
  }

}

object PolymorphicTypeTest {

  import PolymorphicType._

  val chair = Chair(4, 1)
  val rawChair = s"""{
    | "legs":${chair.legs},
    | "occupancy":${chair.occupancy}}""".stripMargin

  val table = Table(1, 6)
  val rawTable = s"""{
    | "legs":${table.legs}
    | "capacity":${table.capacity}}""".stripMargin

}
