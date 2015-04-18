package com.adlawson.json4s

import org.json4s.{DefaultFormats, FieldSerializer, Serializer}
import scala.language.existentials

object ExtendedFormats extends ExtendedFormats

trait ExtendedFormats extends DefaultFormats {
  override val strict: Boolean = true

  override val customSerializers: List[Serializer[_]] = List(
    CharSerializer
  )

  override val fieldSerializers: List[(Class[_], FieldSerializer[_])] = List(
    TypeFieldSerializer.mf.runtimeClass -> TypeFieldSerializer
  )
}
