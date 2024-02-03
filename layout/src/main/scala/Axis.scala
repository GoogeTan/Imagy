package me.katze.imagy.layout

enum Axis:
  case Vertical
  case Horizontal
end Axis

type AnotherAxis[T <: Axis] <: Axis = T match {
  case Axis.Vertical.type => Axis.Horizontal.type
  case Axis.Horizontal.type => Axis.Vertical.type
}