package com.adlawson.json4s

import org.json4s.{CustomSerializer, JString}

object CharSerializer extends CustomSerializer[Char](format => (
  { case JString(x) if x.length == 1 => x.head },
  { case x: Char => JString(x.toString) }
))
