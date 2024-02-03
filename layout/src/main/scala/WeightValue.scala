package me.katze.imagy.layout

import bound.{ AxisDependentBounds, Bounds }
import bound.constraints.{ IndependentConstraint, MainAxisConstraint }
import unit.MeasurementUnit
import unit.constraints.Finite

import cats.syntax.all.given
import io.github.iltotore.iron.constraint.all.*
import io.github.iltotore.iron.{ *, given }
import constraint.{ *, given }
import me.katze.imagy.common.{ Nat, ZNat }
import me.katze.imagy.components.layout.MaybeWeighted
import me.katze.imagy.layout.constraint.{ *, given }

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

def matchElements[T](
                          elements : List[MaybeWeighted[Measurable[T]]],
                        ) : Either[List[MaybeWeighted[Measurable[T]]] :| Exists[Weighted], List[Measurable[T]]] =
  elements.refineOption[Exists[Weighted]].toLeft(elements.map(_.value))
end matchElements

def weightValueFor[T](
                          elements : List[MaybeWeighted[Measurable[T]]] :| Exists[Weighted],
                          constraints: AxisDependentBounds
                        ) : WeightValue =
  val derefined : List[MaybeWeighted[Measurable[T]]] = elements
  val allTheWeight = derefined.mapFilter(_.weight).sum.refine[Positive]
  val freeHeight : Option[ZNat] = (mainAxisMaxValue(constraints).value - fixedSpace(elements, constraints)).refineOption
  // Если фиксированные элементы занимают больше дозволенного, то для весовых места нет, поэтому 0.
  // Это допущение необходимо, чтобы не получить отрицательное количество пустого места.
  WeightValue(allTheWeight, freeHeight.getOrElse(0))
end weightValueFor

def mainAxisMaxValue(constraints: AxisDependentBounds) : MeasurementUnit.Value =
  constraints.mainAxis.max match
    case res : MeasurementUnit.Value => res
    case _ => throw Exception("Impossible by type system. Please create an issue in case this happened.")
  end match
end mainAxisMaxValue

def fixedSpace[T](children : List[MaybeWeighted[Measurable[T]]], constraints: AxisDependentBounds) : ZNat =
  children.map {
    case MaybeWeighted(value, None) =>
      // Тут мы уверены в этом. 
      value.placeInside(constraints.bounds.assume).mainAxisValue(constraints.axis)
    case _ => 0
  }.sum.refine // Тут мы уверены, что значение >= 0, так как это сумма ZNat. Просто это нельзя по человечески выразить в типе.
end fixedSpace
