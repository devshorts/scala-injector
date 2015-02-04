package data.models

import Conversions._

case class Name(inner : String)

object Conversions{
  implicit def toData(i : Name) : String = i.inner
}

case class Age(inner : String)

class Sample {

  val x : Name = Name("foo")
  val y : Age = Age("foo")

  def uses(a : Name) : String = a

}
