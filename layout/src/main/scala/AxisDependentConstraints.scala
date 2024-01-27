package me.katze.imagy.layout

import me.katze
import me.katze.imagy
import me.katze.imagy.layout

final case class AxisDependentConstraints(mainAxis : AxisConstraints, additionalAxis : AxisConstraints, axis : Axis):
  def isMainAxisBounded : Boolean =
    mainAxis.bounded
  end isMainAxisBounded
  
  def mainAxisMaxValue : Int =
    mainAxis.max
  end mainAxisMaxValue
  
  def withMainAxisMaxValue(value : Int) : AxisDependentConstraints =
    copy(mainAxis = mainAxis.withMaxValue(value))
  end withMainAxisMaxValue
  
  def additionalAxisMaxValue : Int =
    additionalAxis.max
  end additionalAxisMaxValue
  
  def constraints : Constraints =
    axis match
      case Axis.Vertical => Constraints(additionalAxis, mainAxis)
      case Axis.Horizontal => Constraints(mainAxis, additionalAxis)
    end match
  end constraints
end AxisDependentConstraints

object AxisDependentConstraints:
  def fromConstraints(constraints: Constraints, axis: Axis) : AxisDependentConstraints = AxisDependentConstraints(
    if axis == Axis.Vertical then constraints.vertical else constraints.horizontal,
    if axis == Axis.Vertical then constraints.horizontal else constraints.vertical,
    axis
  )
  