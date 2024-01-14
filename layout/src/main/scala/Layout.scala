package me.katze.imagy.layout

trait Layout[T]:
  def apply(children : List[Placed[T]]) : Sized[T]
end Layout
