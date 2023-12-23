package me.katze.imagy
package midle

case class Placed[+T](value : T, rect : Rect):
  def minX: Int = rect.x
  def maxX: Int = rect.x + rect.width
  def minY: Int = rect.y
  def maxY: Int = rect.y + rect.height
  
  def height : Int = rect.height
  
  def width : Int = rect.width
end Placed
