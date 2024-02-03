package me.katze.imagy.layout
package bound.constraints

import bound.{ AxisBounds, AxisDependentBounds, Bounds }

import io.github.iltotore.iron.Constraint

final class AxisConstraint[T <: Axis, C]

given[T <: Axis, C](using c : Constraint[AxisBounds, C]): Constraint[Bounds, AxisConstraint[T, C]] with
  override inline def test(value: Bounds): Boolean =
    compiletime.constValue[T] match
      case Axis.Vertical => c.test(value.vertical)
      case Axis.Horizontal => c.test(value.horizontal)
    end match
  end test
  
  override inline def message: String = s"${compiletime.constValue[T]} axis must: ${c.message}"
end given
