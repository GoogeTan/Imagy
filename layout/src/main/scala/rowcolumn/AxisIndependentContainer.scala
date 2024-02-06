package me.katze.imagy.layout
package rowcolumn

import bound.AxisDependentBounds
import bound.constraints.{ MainAxisConstraint, given }

import io.github.iltotore.iron.constraint.all.{ *, given }
import io.github.iltotore.iron.constraint.collection.{ *, given }
import io.github.iltotore.iron.{ *, given }
import unit.constraints.Finite

import me.katze.imagy.components.layout.MaybeWeighted
import me.katze.imagy.components.layout.strategy.{ AdditionalAxisStrategy, Begin }
import me.katze.imagy.layout.constraint.Weighted

def column[T : Layout](
                        elements : List[MaybeWeighted[Measurable[T]]] :| Exists[Weighted],
                        horizontalStrategy : AdditionalAxisStrategy
                      ) : Measurable[T] =
  WeightedAxisBasedContainerMeasurable(Axis.Vertical, horizontalStrategy, elements)
end column

def WeightedAxisBasedContainerMeasurable[T : Layout](
                                                      mainAxis: Axis,
                                                      additionalAxisStrategy: AdditionalAxisStrategy,
                                                      elements : List[MaybeWeighted[Measurable[T]]] :| Exists[Weighted],
                                                    ) : Measurable[T] =
  constraints =>
    val dependentAxes: AxisDependentBounds :| MainAxisConstraint[Finite] = AxisDependentBounds.fromConstraints(constraints, mainAxis).refine
    val measured = measure(elements, dependentAxes)
    val placed = rowColumnPlace(measured, Begin, additionalAxisStrategy, dependentAxes)
    summon[Layout[T]](placed)
end WeightedAxisBasedContainerMeasurable
