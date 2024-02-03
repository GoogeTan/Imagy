package me.katze.imagy.layout
package bound.constraints

import bound.*

import io.github.iltotore.iron.{ *, given }

import scala.compiletime.summonInline

final class MainAxisConstraint[C]

inline given mainAxisConstraint[C, Impl <: Constraint[AxisBounds, C]](using Impl): Constraint[AxisDependentBounds, MainAxisConstraint[C]] with
  override inline def test(value: AxisDependentBounds): Boolean =
    summonInline[Impl].test(value.mainAxis)
  end test
  
  override inline def message: String = s"Main axis must: ${summonInline[Impl].message}"
end mainAxisConstraint

final class AdditionalAxisConstraint[C]

inline given additionalAxisConstraint[C, Impl <: Constraint[AxisBounds, C]](using Impl): Constraint[AxisDependentBounds, AdditionalAxisConstraint[C]] with
  override inline def test(value: AxisDependentBounds): Boolean =
    summonInline[Impl].test(value.additionalAxis)
  end test
  
  override inline def message: String = s"Main axis must: ${summonInline[Impl].message}"
end additionalAxisConstraint
