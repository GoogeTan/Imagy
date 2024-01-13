package me.katze.imagy.layout

trait Placeable[+T]:
  def placeInside(rect : Rect) : Placed[T]
end Placeable
