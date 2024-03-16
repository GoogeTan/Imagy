package me.katze.imagy.example
package place

import me.katze.imagy.layout.bound.Bounds

trait Placeable[T]:
  def place(bounds : Bounds) : T
end Placeable
