package com.adlawson.json4s

import org.json4s._

/**
 * Serialize scala Types and Type Aliases
 *
 * A is the Type Alias
 * B is a constructor type for the type alias
 *
 * The constructor type is needed only during the [de]serialization stages
 * and isn't exposed in any other way.
 *
 * Example:
 * ```
 *   import com.adlawson.json4s._
 *   import org.json4s.DefaultFormats
 *
 *   type FooBar = Foo with Bar
 *
 *   case class Foo(a: Int)
 *   trait Bar { def b: Int }
 *
 *   // Dirty type alias constructor
 *   private case class _FooBar(a: Int, b: Int)
 *
 *   // Serializer
 *   object FooBarSerializer extends TypeSerializer[FooBar, _FooBar] {
 *     override def wrap(f: FooBar) = _FooBar(f.a, f.b)
 *     override def unwrap(f: _FooBar) = new Foo(f.a) with Bar { val b = f.b }
 *   }
 *
 *   // Add to implicit formats
 *   implicit val format = DefaultFormats + FooBarSerializer
 * ```
 */
abstract class TypeSerializer[A : Manifest, B : Manifest] extends Serializer[A] {
  def unwrap(b: B): A
  def wrap(a: A): B

  protected val Class = implicitly[Manifest[A]].runtimeClass

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), A] = {
    case (TypeInfo(Class, _), json) => json match {
      case JNull => null.asInstanceOf[A]
      case value: JValue if (value.extractOpt[B].isDefined) => unwrap(value.extract[B])
      case value => throw new MappingException(s"Can't convert $value to $Class")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case a: A => Extraction.decompose(wrap(a))
  }
}
