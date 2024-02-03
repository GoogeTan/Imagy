package me.katze.imagy.layout
package bound

final case class AxisDependentBounds(mainAxis : AxisBounds, additionalAxis : AxisBounds, axis : Axis):
  val bounds : Bounds =
    axis match
      case Axis.Vertical => Bounds(additionalAxis, mainAxis)
      case Axis.Horizontal => Bounds(mainAxis, additionalAxis)
    end match
  end bounds
end AxisDependentBounds

object AxisDependentBounds:
  def fromConstraints(constraints: Bounds, axis: Axis) : AxisDependentBounds = AxisDependentBounds(
    if axis == Axis.Vertical then constraints.vertical else constraints.horizontal,
    if axis == Axis.Vertical then constraints.horizontal else constraints.vertical,
    axis
  )
  end fromConstraints
end AxisDependentBounds
