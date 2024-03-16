package me.katze.imagy.layout
package bound.constraints

import bound.AxisDependentBounds
import unit.constraints.Finite

import io.github.iltotore.iron.:|
import me.katze.imagy.common.ZNat

final class AdditionalAxisConstraint[C]

object AdditionalAxisConstraint:
  def mainAxisValue(bounds: AxisDependentBounds :| AdditionalAxisConstraint[Finite]): ZNat =
    bounds.additionalAxisMaxValue.getOrElse(throw new IllegalStateException("Never happends. In case this happened create an issue."))
  end mainAxisValue
end AdditionalAxisConstraint
