package me.katze.imagy.layout

final case class Placed[T](value : T, x : Int, y : Int, width : Int, height : Int):
  def this(sized : Sized[T], x : Int, y : Int) = this(sized.value, x, y, sized.width, sized.height)