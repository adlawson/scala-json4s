package com.adlawson.json4s

object NamedType {

  sealed trait Payment { def amount: Long }
  case class Card(amount: Long) extends Payment
  case class Cash(amount: Long) extends Payment

  // NB. Use alongside TypeFieldSerializer to serialize `typ` field to `type`
  private[json4s] case class _PaymentConst(amount: Long, typ: String)

  object PaymentSerializer extends TypeSerializer[Payment, _PaymentConst] {
    override def wrap(p: Payment) = p match {
      case Card(a) => _PaymentConst(a, "card")
      case Cash(a) => _PaymentConst(a, "cash")
    }
    override def unwrap(p: _PaymentConst) = p match {
      case _PaymentConst(a, "card") => Card(a)
      case _PaymentConst(a, "cash") => Cash(a)
    }
  }

}
