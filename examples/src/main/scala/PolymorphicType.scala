package com.adlawson.json4s

object PolymorphicType {

  sealed trait Furniture

  case class Chair(legs: Int, occupancy: Int) extends Furniture
  case class Table(legs: Int, capacity: Int) extends Furniture

  private[json4s] case class _FurnitureConst(
    legs: Int, occupancy: Option[Int], capacity: Option[Int])

  object FurnitureSerializer extends TypeSerializer[Furniture, _FurnitureConst] {
    override def wrap(g: Furniture) = g match {
      case Chair(l, o) => _FurnitureConst(l, Some(o), None)
      case Table(l, c) => _FurnitureConst(l, None, Some(c))
    }
    override def unwrap(g: _FurnitureConst) = g match {
      case _FurnitureConst(l, Some(o), _) => Chair(l, o)
      case _FurnitureConst(l, _, Some(c)) => Table(l, c)
    }
  }

}
