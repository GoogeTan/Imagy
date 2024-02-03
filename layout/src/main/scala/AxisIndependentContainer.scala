package me.katze.imagy.layout

import bound.AxisDependentBounds
import bound.constraints.{ MainAxisConstraint, given }

import io.github.iltotore.iron.constraint.all.{ *, given }
import io.github.iltotore.iron.constraint.collection.{ *, given }
import io.github.iltotore.iron.{ *, given }
import me.katze.imagy.layout.constraint.Weighted
import unit.constraints.Finite

import me.katze.imagy.components.layout.MaybeWeighted
import me.katze.imagy.components.layout.strategy.AdditionalAxisStrategy

def column[T](
                elements : List[MaybeWeighted[Measurable[T]]] :| Exists[Weighted],
                horizontalStrategy : AdditionalAxisStrategy,
                layout: Layout[T]
              ) : Measurable[T] =
  WeightedAxisBasedContainerMeasurable(Axis.Vertical, layout, horizontalStrategy, elements)
end column

def WeightedAxisBasedContainerMeasurable[T](
                                              mainAxis: Axis,
                                              layout   : Layout[T],
                                              additionalAxisStrategy: AdditionalAxisStrategy,
                                              elements : List[MaybeWeighted[Measurable[T]]] :| Exists[Weighted],
                                            ) : Measurable[T] =
  constraints =>
    val dependentAxes: AxisDependentBounds :| MainAxisConstraint[Finite] = AxisDependentBounds.fromConstraints(constraints, mainAxis).refine
    val measured = measure(elements, dependentAxes)
    val placed = place(measured, additionalAxisStrategy, dependentAxes)
    layout(placed)
end WeightedAxisBasedContainerMeasurable
