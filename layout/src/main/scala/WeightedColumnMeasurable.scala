package me.katze.imagy.layout

import cats.syntax.all.{ *, given }
import io.github.iltotore.iron.constraint.all.{ GreaterEqual, Positive }
import io.github.iltotore.iron.{ *, given }
import me.katze.imagy.common.{ Nat, ZNat }
import me.katze.imagy.components.layout.{ AdditionalAxisStrategy, MainAxisStrategy, MaybeWeighted }

final case class ColumnStrategy(verticalStrategy : MainAxisStrategy = MainAxisStrategy.Begin, horizontalStrategy : AdditionalAxisStrategy = AdditionalAxisStrategy.Center)

final class WeightedColumnMeasurable[T](
                                          val elements           : List[MaybeWeighted[Measurable[T]]],
                                          val layout             : Layout[T],
                                          val strategy : ColumnStrategy
                                        ) extends Measurable[T]:
  override def placeInside(constraints: Constraints): Sized[T] =
    layout(placeAllTheElements(elements, constraints, strategy))
  end placeInside
end WeightedColumnMeasurable

def placeAllTheElements[T](
                            children: List[MaybeWeighted[Measurable[T]]],
                            constraints: Constraints,
                            strategy : ColumnStrategy
                          ): List[Placed[T]] =
  val measured = measure(children, constraints)
  place(measured, constraints, strategy)
end placeAllTheElements

def measure[T](children : List[MaybeWeighted[Measurable[T]]], constraints: Constraints) : List[Sized[T]] =
  weightValueFor(children, constraints) match
    case Left(weightContext) =>
      measureWithContext(children, weightContext, constraints)
    case Right(nonWeightedChildren) =>
      assert(nonWeightedChildren.size == children.size, "Weight calculation must never affect children count. In case it happened, please, create an issue.")
      nonWeightedChildren.map(_.placeInside(constraints))
  end match
end measure

def measureWithContext[T](children : List[MaybeWeighted[Measurable[T]]], context: WeightValue, constraints: Constraints) : List[Sized[T]] =
  children.map {
    case MaybeWeighted(value, None) =>
      value.placeInside(constraints)
    case MaybeWeighted(value, Some(weight)) =>
      value.placeInside(constrainsWithWeight(weight, context, constraints))
  }
end measureWithContext

def constrainsWithWeight(weight : Nat, context: WeightValue, constraints: Constraints) : Constraints =
  constraints.withMaxHeight(context.weightsSpace(weight))
end constrainsWithWeight

// Отображает то, сколько места занимает каждая единица веса. TODO придумать название получше на английском.
final case class WeightValue(allTheWeight : Nat, freeSpace : ZNat):
  def weightsSpace(weight : Nat) : ZNat =
    /*
     * if - защита от переполнения.
     * Мы уверены в refine, так как произведение и частное неотрицательных всегда не отрицательно.
     */
    if Int.MaxValue / weight >= freeSpace || Int.MaxValue / freeSpace >= weight then
      (weight / allTheWeight * freeSpace).refine
    else
      (weight * freeSpace / allTheWeight).refine
    end if
  end weightsSpace
end WeightValue

def weightValueFor[T](children : List[MaybeWeighted[Measurable[T]]], constraints: Constraints) : Either[WeightValue, List[Measurable[T]]] =
  allTheWeightFor(children, constraints).toLeft(children.map(_.value))
end weightValueFor

def allTheWeightFor[T](children : List[MaybeWeighted[Measurable[T]]], constraints: Constraints) : Option[WeightValue] =
    children
      .mapFilter(_.weight)
      .sum
      .refineOption[Positive]
      .map(allTheWeight =>
        assert(constraints.hasBoundedHeight, "Container with weighted children must have fixed size.")
        val freeHeight : Option[ZNat] = (constraints.maxHeight - fixedHeight(children, constraints)).refineOption
        // Если фиксированные элементы занимают больше дозволенного, то для весовых места нет, поэтому 0.
        // Это допущение необходимо, чтобы не получить отрицательное количество пустого места.
        WeightValue(allTheWeight, freeHeight.getOrElse(0))
      )
end allTheWeightFor

def fixedHeight[T](children : List[MaybeWeighted[Measurable[T]]], constraints: Constraints) : ZNat =
  children.map {
    case MaybeWeighted(value, None) =>
      value.placeInside(constraints).height
    case _ => 0
  }.sum.refine // Тут мы уверены, что значение >= 0, так как это сумма ZNat. Просто это нельзя по человечески выразить в типе.
end fixedHeight

def place[T](sized : List[Sized[T]], constraints: Constraints, strategy: ColumnStrategy) : List[Placed[T]] =
  ???
end place
