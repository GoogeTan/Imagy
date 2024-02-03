package me.katze.imagy.layout
package bound.constraints

import bound.AxisBounds
import unit.MeasurementUnit
import unit.constraints.Infinite

import io.github.iltotore.iron.Constraint

given axisBoundsInfiniteConstraint : Constraint[AxisBounds, Infinite] with
  override inline def test(value: AxisBounds): Boolean = value.max == MeasurementUnit.Infinite
  
  override inline def message: String = "AxisBounds must have infinite maximum size"
end axisBoundsInfiniteConstraint
