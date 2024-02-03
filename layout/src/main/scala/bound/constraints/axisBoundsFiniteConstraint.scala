package me.katze.imagy.layout
package bound.constraints

import bound.AxisBounds
import unit.MeasurementUnit
import unit.constraints.Finite

import io.github.iltotore.iron.Constraint

given axisBoundsFiniteConstraint : Constraint[AxisBounds, Finite] with
  override inline def test(value: AxisBounds): Boolean = value.max != MeasurementUnit.Infinite
  override inline def message: String = "AxisBounds must have fixed maximum size."
end axisBoundsFiniteConstraint
