package me.katze.imagy.layout
package bound.constraints

import bound.{ AxisDependentBounds, Bounds }

import io.github.iltotore.iron.Constraint

import scala.compiletime.summonInline

final class IndependentConstraint[T]

inline given independentConstraints[T, Impl <: Constraint[Bounds, T]](using Impl): Constraint[AxisDependentBounds, IndependentConstraint[T]] with
  override inline def test(value: AxisDependentBounds): Boolean = summonInline[Impl].test(value.bounds)
  
  override inline def message: String = "TODO MESSAGE"
  