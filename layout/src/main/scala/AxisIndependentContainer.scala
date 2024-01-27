package me.katze.imagy.layout

import me.katze.imagy.components.layout.{ AdditionalAxisStrategy, MainAxisStrategy, MaybeWeighted }

final case class AxisBasedContainerStrategy(mainAxisStrategy : MainAxisStrategy, additionalAxisStrategy : AdditionalAxisStrategy)

def WeightedAxisBasedContainerMeasurable[T](
                                              elements : List[MaybeWeighted[Measurable[T]]],
                                              layout   : Layout[T],
                                              strategy : AxisBasedContainerStrategy,
                                              axis: Axis
                                            ) : Measurable[T] =
  constraints =>
    val dependentAxes = AxisDependentConstraints.fromConstraints(constraints, axis)
    val measured = measure(elements, dependentAxes, axis)
    val placed = place(measured, dependentAxes, strategy, axis)
    layout(placed)
end WeightedAxisBasedContainerMeasurable

def AxisBasedContainerMeasurable[T](
                                      elements : List[Measurable[T]],
                                      layout   : Layout[T],
                                      strategy : AxisBasedContainerStrategy,
                                      axis: Axis
                                    ) : Measurable[T] =
  constraints =>
    val measured = measureWithoutWeight(elements, constraints)
    val dependentAxes = AxisDependentConstraints.fromConstraints(constraints, axis)
    val placed = place(measured, dependentAxes, strategy, axis)
    layout(placed)
end AxisBasedContainerMeasurable
