package me.katze.imagy.layout

import bound.AxisDependentBounds
import unit.MeasurementUnit.Value

import io.github.iltotore.iron.constraint.all.{ *, given }
import io.github.iltotore.iron.{ *, given }
import me.katze.imagy.layout.constraint.Weighted

import me.katze.imagy.common.Nat
import me.katze.imagy.components.layout.MaybeWeighted

def measure[T](children : List[MaybeWeighted[Measurable[T]]] :| Exists[Weighted], constraints: AxisDependentBounds) : List[Sized[T]] =
  val weightValue = weightValueFor(children, constraints)
  measureWithWeight(children, weightValue, constraints)
end measure

def measureWithWeight[T](children : List[MaybeWeighted[Measurable[T]]], context: WeightValue, constraints: AxisDependentBounds) : List[Sized[T]] =
  children.map:
    case MaybeWeighted(value, None) =>
      value.placeInside(constraints.bounds)
    case MaybeWeighted(value, Some(weight)) =>
      value.placeInside(constrainsWithWeight(weight, context, constraints).bounds)
end measureWithWeight

def constrainsWithWeight(weight : Nat, context: WeightValue, constraints: AxisDependentBounds) : AxisDependentBounds =
  constraints.copy(mainAxis = constraints.mainAxis.withMaxValue(Value(context.spaceForWeight(weight))))
end constrainsWithWeight

