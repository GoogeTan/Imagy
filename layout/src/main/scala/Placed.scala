package me.katze.imagy.layout

import me.katze
import me.katze.imagy
import me.katze.imagy.common.ZNat
import me.katze.imagy.layout

final case class Placed[+T](value : T, x : ZNat, y : ZNat, width : ZNat, height : ZNat):
  def this(sized : Sized[T], x : ZNat, y : ZNat) = this(sized.value, x, y, sized.width, sized.height)
  
  def axisCoordinate(axis : Axis) : ZNat =
    axis match
      case Axis.Vertical => y
      case Axis.Horizontal => x
    end match
  end axisCoordinate
  
  def axisValue(axis: Axis) : ZNat =
    axis match
      case Axis.Vertical => height
      case Axis.Horizontal => width
    end match
  end axisValue
end Placed
