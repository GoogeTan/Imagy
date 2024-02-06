package me.katze.imagy.layout
package bound

import unit.MeasurementUnit
import unit.constraints.{ Finite, Infinite }

import io.github.iltotore.iron.{ *, given }

final case class AxisBounds(min: MeasurementUnit, max: MeasurementUnit):
  assert(min <= max)
  
  def fixed: Boolean = min == max
  def finite : Boolean = max != MeasurementUnit.Infinite
  def zero : Boolean = max == MeasurementUnit.Value(0)
  
  def withMaxValue(value : MeasurementUnit) : AxisBounds =
    AxisBounds(Ordering[MeasurementUnit].min(this.min, value), value)
  end withMaxValue
end AxisBounds
