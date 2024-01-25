package me.katze.imagy.layout

import cats.syntax.all.{ *, given }
import io.github.iltotore.iron.constraint.all.Positive
import me.katze.imagy.common.{ Nat, ZNat }
import io.github.iltotore.iron.{ *, given }
import me.katze.imagy.components.layout.MaybeWeighted

// Отображает то, сколько места занимает каждая единица веса. TODO придумать название получше на английском.
final case class WeightValue(allTheWeight: Nat, freeSpace: ZNat):
  def spaceForWeight(weight: Nat): ZNat =
    /*
     * if - защита от переполнения.
     * Мы уверены в refine, так как произведение и частное неотрицательных всегда не отрицательно.
     */
    if Int.MaxValue / weight >= freeSpace || Int.MaxValue / freeSpace >= weight then
      (weight / allTheWeight * freeSpace).refine
    else
      (weight * freeSpace / allTheWeight).refine
    end if
  end spaceForWeight
end WeightValue

def weightValueFor[T](elements : List[MaybeWeighted[Measurable[T]]], constraints: Constraints, axis: Axis) : Either[WeightValue, List[Measurable[T]]] =
  allTheWeightFor(elements, constraints, axis).toLeft(elements.map(_.value))
end weightValueFor

// TODO Отрефакторить это чудо. Читается очень не очень.
def allTheWeightFor[T](children : List[MaybeWeighted[Measurable[T]]], constraints: Constraints, axis: Axis) : Option[WeightValue] =
  children
    .mapFilter(_.weight)
    .sum
    .refineOption[Positive]
    .map(allTheWeight =>
      assert(constraints.isAxisBounded(axis), s"Container with weighted children must have fixed size for axis $axis.")
      val freeHeight : Option[ZNat] = (constraints.maxValue(axis) - fixedSpace(children, constraints, axis)).refineOption
      // Если фиксированные элементы занимают больше дозволенного, то для весовых места нет, поэтому 0.
      // Это допущение необходимо, чтобы не получить отрицательное количество пустого места.
      WeightValue(allTheWeight, freeHeight.getOrElse(0))
    )
end allTheWeightFor

def fixedSpace[T](children : List[MaybeWeighted[Measurable[T]]], constraints: Constraints, axis: Axis) : ZNat =
  children.map {
    case MaybeWeighted(value, None) =>
      value.placeInside(constraints).mainAxisValue(axis)
    case _ => 0
  }.sum.refine // Тут мы уверены, что значение >= 0, так как это сумма ZNat. Просто это нельзя по человечески выразить в типе.
end fixedSpace