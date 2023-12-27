package me.katze.imagy
package midle

import common.Nat

trait Placeable[+T]:
  val weight : Option[Nat]
  def placeInside(rect : Rect) : Placed[T]
end Placeable
