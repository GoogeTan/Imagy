package me.katze.imagy.layout

trait Layout[T]:
  def apply(elements : List[Placed[T]]) : Sized[T]
end Layout
