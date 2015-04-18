package com.adlawson.json4s

import org.scalatest.{FlatSpec, Matchers}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}

class NamedTypeTest extends FlatSpec with Matchers {

  import NamedType._
  import NamedTypeTest._

  implicit val formats = DefaultFormats + PaymentSerializer

  "read" must "deserialize json and extract it into a Card record" in {
    val result = read[Card](rawCard)

    result shouldBe a[Card]
    result shouldBe(card)
  }

  it must "deserialize json and extract it into a Cash record" in {
    val result = read[Cash](rawCash)

    result shouldBe a[Cash]
    result shouldBe(cash)
  }

  it must "deserialize json and extract it into a polymorphic list of Payment records" in {
    val results = read[List[Payment]](s"""[$rawCard,$rawCash]""")

    results.head shouldBe a[Card]
    results.last shouldBe a[Cash]
    results.head shouldBe(card)
    results.last shouldBe(cash)
  }

  "write" must "serialize a Card record into a valid json string" in {
    val json = write(card)
    val done = read[Map[String, _]](json)

    done shouldBe(Map(
      "amount"->card.amount,
      "typ"->"card"))
  }

  it must "serialize a Cash record into a valid json string" in {
    val json = write(cash)
    val done = read[Map[String, _]](json)

    done shouldBe(Map(
      "amount"->cash.amount,
      "typ"->"cash"))
  }

  it must "serialize a polymorphic list of Payment records into a valid json string" in {
    val json = write(List(card, cash))
    val done = read[List[Map[String, _]]](json)

    done.head shouldBe(Map(
      "amount"->card.amount,
      "typ"->"card"))

    done.last shouldBe(Map(
      "amount"->cash.amount,
      "typ"->"cash"))
  }

}

object NamedTypeTest {

  import NamedType._

  val card = Card(1234L)
  val rawCard = s"""{
    | "amount":${card.amount},
    | "typ":"card"}""".stripMargin

  val cash = Cash(5678L)
  val rawCash = s"""{
    | "amount":${cash.amount}
    | "typ":"cash"}""".stripMargin

}
