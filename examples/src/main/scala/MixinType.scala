package com.adlawson.json4s

object MixinType {

  type FooBar = Foo with Bar

  case class Foo(a: Int)
  trait Bar { def b: Int }

  private[json4s] case class _FooBarConst(a: Int, b: Int)

  object FooBarSerializer extends TypeSerializer[FooBar, _FooBarConst] {
    override def wrap(f: FooBar) = _FooBarConst(f.a, f.b)
    override def unwrap(f: _FooBarConst) = new Foo(f.a) with Bar { val b = f.b }
  }

}
