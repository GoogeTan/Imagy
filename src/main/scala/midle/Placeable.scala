package me.katze.imagy
package midle

trait Placeable[+T]:
  val weight : Option[Int]
  def placeInside(rect : Rect) : Placed[T]
end Placeable
