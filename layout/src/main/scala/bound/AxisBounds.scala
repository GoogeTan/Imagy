package me.katze.imagy.layout
package bound

import bound.constraints.given
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
  
  def refined : Either[AxisBounds :| Finite, AxisBounds :| Infinite] =
    Either.cond(this.finite, this.refine, this.refine)
  end refined
end AxisBounds
