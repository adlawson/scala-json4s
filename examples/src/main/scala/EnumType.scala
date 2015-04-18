package com.adlawson.json4s

import org.json4s.MappingException

object EnumType {

  sealed trait Enum {
    val value: String = toString.toLowerCase
  }

  sealed trait Direction extends Enum
  object Direction {
    case object Left extends Direction
    case object Right extends Direction
  }

  object DirectionSerializer extends TypeSerializer[Direction, String] {
    override def wrap(dir: Direction) = dir.value
    override def unwrap(s: String) = s match {
      case Direction.Left.value  => Direction.Left
      case Direction.Right.value => Direction.Right
      case s => throw new MappingException(s"Can't convert $s to $Class")
    }
  }

}
