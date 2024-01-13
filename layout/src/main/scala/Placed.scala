package me.katze.imagy.layout

import me.katze.imagy.common.ZNat

final case class Placed[+T](value : T, rect : Rect):
  def height: ZNat = rect.height
  def width: ZNat = rect.width
end Placed
