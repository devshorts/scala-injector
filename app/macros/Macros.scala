package foo.macros

import scala.language.experimental.macros
import scala.reflect.macros.whitebox._

object Macros {
  def foo() : Unit = macro foo_impl
  def foo_impl(c: Context)() = {
    import c.universe._
    reify { println("fooasdfasdf") }
  }
}