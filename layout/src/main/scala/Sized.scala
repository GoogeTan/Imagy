package me.katze.imagy.layout

import me.katze.imagy.common.ZNat

// TODO имена получше для методов. Не отражают суть.
final case class Sized[+T](value : T, width : ZNat, height : ZNat):
  def mainAxisValue(axis : Axis) : ZNat =
    axis match
      case Axis.Vertical => height
      case Axis.Horizontal => width
    end match
  end mainAxisValue
  
  def additionalAxisValue(axis: Axis): ZNat =
    axis match
      case Axis.Vertical => width
      case Axis.Horizontal => height
    end match
  end additionalAxisValue
end Sized
