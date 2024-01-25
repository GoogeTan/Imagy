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
    val measured = measure(elements, AxisDependentConstraints(constraints, axis), axis)
    val placed = place(measured, constraints, strategy, axis)
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
    val placed = place(measured, constraints, strategy, axis)
    layout(placed)
end AxisBasedContainerMeasurable
