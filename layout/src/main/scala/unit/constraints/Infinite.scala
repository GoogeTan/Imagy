package me.katze.imagy.layout
package unit.constraints

import unit.MeasurementUnit

import io.github.iltotore.iron.Constraint

final class Infinite

given Constraint[MeasurementUnit, Infinite] with
  override inline def message: String = "MeasurementUnit must have infinite size."
  
  override inline def test(value: MeasurementUnit): Boolean = value == MeasurementUnit.Infinite
end given
