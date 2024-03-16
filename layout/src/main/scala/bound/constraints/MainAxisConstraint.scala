package me.katze.imagy.layout
package bound.constraints

import bound.*
import unit.MeasurementUnit
import unit.constraints.Finite

import io.github.iltotore.iron.{ *, given }
import me.katze.imagy.common.ZNat

import java.lang.IllegalStateException
import scala.compiletime.summonInline

final class MainAxisConstraint[C]

object MainAxisConstraint:
  def mainAxisValue(bounds : AxisDependentBounds :| MainAxisConstraint[Finite]) : ZNat =
    bounds.mainAxisMaxValue.getOrElse(throw new IllegalStateException("Never happends. In case this happened create an issue."))
  end mainAxisValue
end MainAxisConstraint

