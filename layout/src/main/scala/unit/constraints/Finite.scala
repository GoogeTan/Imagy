package me.katze.imagy.layout
package unit.constraints

import unit.MeasurementUnit

import io.github.iltotore.iron.Constraint

final class Finite

given Constraint[MeasurementUnit, Finite] with
  override inline def test(value: MeasurementUnit): Boolean =
    value != MeasurementUnit.Infinite
  
  override inline def message: String = "MeasurementUnit must have finite size."
end given
