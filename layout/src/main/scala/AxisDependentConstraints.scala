package me.katze.imagy.layout

final case class AxisDependentConstraints(constraints: Constraints, axis : Axis):  
  def isMainAxisBounded : Boolean =
    axis match
      case Axis.Vertical => constraints.hasBoundedHeight
      case Axis.Horizontal => constraints.hasBoundedWidth
    end match
  end isMainAxisBounded
  
  def mainAxisMaxValue : Int =
    axis match
      case Axis.Vertical => constraints.maxHeight
      case Axis.Horizontal => constraints.maxWidth
    end match
  end mainAxisMaxValue
  
  def withMainAxisMaxValue(value : Int) : AxisDependentConstraints =
    axis match
      case Axis.Vertical =>
        AxisDependentConstraints(constraints.withMaxHeight(value), axis)
      case Axis.Horizontal =>
        AxisDependentConstraints(constraints.withMaxWidth(value), axis)
  end withMainAxisMaxValue
end AxisDependentConstraints
