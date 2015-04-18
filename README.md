# JSON4S Extensions

<img src="https://raw.githubusercontent.com/adlawson/scala-json4s/master/logo.png" alt=JSON4S" align="right" width=200/>

**JSON4S Extensions** is a small collection of serializers, formatters and other
useful extensions for [JSON4S][json4s] in Scala.

### ExtendedFormats
The [`ExtendedFormats`][format] object (and trait) works in exactly the same way
as JSON4S `DefaultFormats`, but with a few other useful serializers included by
default.
```scala
package mypackage

import com.adlawson.json4s.ExtendedFormats
import org.json4s.native.Serializer.{read, write}

implicit val formats = ExtendedFormats

val foo = read[Foo](someJsonString)
val raw = write(foo)
```

Included in `ExtendedFormats` are the following serializers
 - [`CharSerializer`][char] for [de]serializing `Char` field types
 - [`TypeFieldSerializer`][typefield] for mapping a JSON `type` field to `typ` in a class

### TypeSerializer
The abstract [`TypeSerializer`][type] can be used to create custom serializers
for complex types, including types with mixins and matching trait
implementations.
```scala
package mypackage

import com.adlawson.json4s.TypeSerializer
import org.json4s.DefaultFormats

type FooBar = Foo with Bar

case class Foo(a: Int)
trait Bar { def b: Int }

private[mypackage] case class _FooBar(a: Int, b: Int)

object FooBarSerializer extends TypeSerializer[FooBar, _FooBar] {
  override def wrap(f: FooBar) = _FooBar(f.a, f.b)
  override def unwrap(f: _FooBar) = new Foo(f.a) with Bar { val b = f.b }
}

implicit val format = DefaultFormats + FooBarSerializer

val fbar = read[FooBar]("""{"a":1,"b":2}""")
val json = write(fbar)
```

### Examples
For further use cases and working examples of the different library components,
please see the bundled [`examples`][examples].

## Contributing
Contributions are accepted via Pull Request, but passing unit tests must be
included before it will be considered for merge.

### License
The content of this library is released under the **MIT License** by
**Andrew Lawson**.<br/> You can find a copy of this license in
[`LICENSE`](LICENSE) or at http://opensource.org/licenses/mit.

[examples]: examples/src/main/scala
[json4s]: http://json4s.org
[char]: src/main/scala/CharSerializer.scala
[format]: src/main/scala/ExtendedFormats.scala
[type]: src/main/scala/TypeSerializer.scala
[typefield]: src/main/scala/TypeFieldSerializer.scala
