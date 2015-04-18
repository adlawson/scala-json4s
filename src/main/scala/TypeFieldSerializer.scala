package com.adlawson.json4s

import org.json4s.FieldSerializer

object TypeFieldSerializer extends FieldSerializer[AnyRef](
  FieldSerializer.renameTo("typ", "type"),
  FieldSerializer.renameFrom("type", "typ")
)
