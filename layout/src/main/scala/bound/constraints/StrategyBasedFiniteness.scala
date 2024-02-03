package me.katze.imagy.layout
package bound.constraints

import bound.{ AxisDependentBounds, Bounds }
import io.github.iltotore.iron.{ *, given }
import unit.constraints.Finite
import me.katze.imagy.components.layout.strategy.*

type StrategyBasedFiniteness[T <: AdditionalAxisStrategy] = T match {
  case Begin.type => Pure
  case Center.type => Finite
  case End.type => Finite
}

