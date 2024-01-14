package me.katze.imagy.layout

trait Measurable[+T]:
  def placeInside(constrains: Constraints): Sized[T]
end Measurable
