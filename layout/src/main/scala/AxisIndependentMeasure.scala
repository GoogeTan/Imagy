package me.katze.imagy.layout

import me.katze.imagy.common.Nat
import me.katze.imagy.components.layout.MaybeWeighted

def measure[T](children : List[MaybeWeighted[Measurable[T]]], constraints: AxisDependentConstraints, axis: Axis) : List[Sized[T]] =
  weightValueFor(children, constraints, axis) match
    case Left(weightContext) =>
      measureWithWeight(children, weightContext, constraints)
    case Right(nonWeightedChildren) =>
      assert(nonWeightedChildren.size == children.size, "Weight calculation must never affect children count. In case it happened, please, create an issue.")
      measureWithoutWeight(nonWeightedChildren, constraints.constraints)
  end match
end measure

def measureWithWeight[T](children : List[MaybeWeighted[Measurable[T]]], context: WeightValue, constraints: AxisDependentConstraints) : List[Sized[T]] =
  children.map:
    case MaybeWeighted(value, None) =>
      value.placeInside(constraints.constraints)
    case MaybeWeighted(value, Some(weight)) =>
      value.placeInside(constrainsWithWeight(weight, context, constraints).constraints)
end measureWithWeight

def measureWithoutWeight[T](children : List[Measurable[T]], constraints: Constraints) : List[Sized[T]] =
  children.map(_.placeInside(constraints))
end measureWithoutWeight

def constrainsWithWeight(weight : Nat, context: WeightValue, constraints: AxisDependentConstraints) : AxisDependentConstraints =
  constraints.withMainAxisMaxValue(context.spaceForWeight(weight))
end constrainsWithWeight

