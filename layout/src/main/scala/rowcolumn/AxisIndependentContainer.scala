package me.katze.imagy.layout
package rowcolumn

import bound.constraints.{ AdditionalAxisConstraint, MainAxisConstraint, StrategyBasedFiniteness, given }
import bound.{ AxisBounds, AxisDependentBounds }

import io.github.iltotore.iron.constraint.all.{ *, given }
import io.github.iltotore.iron.constraint.collection.{ *, given }
import io.github.iltotore.iron.{ *, given }
import constraint.Weighted

import me.katze.imagy.components.layout.MaybeWeighted
import me.katze.imagy.components.layout.strategy.{ AdditionalAxisStrategy, Begin }

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
    given additionalAxisStrategy.type = additionalAxisStrategy
    val dependentAxes =
      AxisDependentBounds.fromConstraints(constraints, mainAxis)
        .refineOption[MainAxisConstraint[StrategyBasedFiniteness[Begin.type]]]
          .getOrElse(throw IllegalArgumentException("TODO TEXT"))
        .refineFurtherOption[AdditionalAxisConstraint[StrategyBasedFiniteness[additionalAxisStrategy.type]]]
          .getOrElse(throw IllegalArgumentException("TODO TEXT"))
    val measured = measure(elements, dependentAxes)
    val placed = rowColumnPlace(measured, Begin, additionalAxisStrategy, dependentAxes)
    summon[Layout[T]](placed)
end WeightedAxisBasedContainerMeasurable
