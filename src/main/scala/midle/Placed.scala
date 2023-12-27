package me.katze.imagy
package midle

import common.ZNat

case class Placed[+T](value : T, rect : Rect):
  def height: ZNat = rect.height
  
  def width: ZNat = rect.width
end Placed
