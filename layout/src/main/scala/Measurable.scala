package me.katze.imagy.layout

import bound.Bounds
import io.github.iltotore.iron.{*, given }

trait Measurable[+T]:
  def placeInside(constrains: Bounds): Sized[T]
end Measurable
