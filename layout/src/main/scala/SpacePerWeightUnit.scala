package me.katze.imagy.layout

import bound.AxisDependentBounds

import cats.syntax.all.given
import io.github.iltotore.iron.constraint.all.*
import io.github.iltotore.iron.constraint.given
import io.github.iltotore.iron.{ *, given }
import constraint.{ *, given }
import me.katze.imagy.common.{ Nat, ZNat }
import me.katze.imagy.components.layout.MaybeWeighted
import me.katze.imagy.layout.constraint.Weighted

/**
 * Обозначает количество места, которое занимает каждая единица веса.
 * @param allTheWeight Суммарный вес всех элементов в контейнере
 * @param freeSpace Остаток места после установки не взвешенных элементов.
 */
final case class SpacePerWeightUnit(allTheWeight: Nat, freeSpace: ZNat):
  /**
   * Считает, сколько места занимает виджет с данным весом.
   * @param weight Вес элемента
   * @return Количество места, которое он занимает.
   */
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
end SpacePerWeightUnit

def spacePerWeightForContainerElements[T](
                                            elements : List[MaybeWeighted[Measurable[T]]] :| Exists[Weighted],
                                            constraints: AxisDependentBounds
                                          ) : SpacePerWeightUnit =
  // Вывод типов не может найти метод из кошек, если не убрать ограничение.
  val derefined : List[MaybeWeighted[Measurable[T]]] = elements
  val allTheWeight = derefined.mapFilter(_.weight).sum.refine[Positive]
  val allTheSpace = constraints.mainAxisMaxValue.get
  val nonWeightedElementsSpace = fixedSpace(elements, constraints)
  if nonWeightedElementsSpace > allTheSpace then
    // Если фиксированные элементы заняли больше места, чем было свободного, то на взвешенные элементы места не остаётся.
    SpacePerWeightUnit(allTheWeight, 0)
  else
    // Мы уверены, что refine пройдёт успешно, так как мы выше сделали проверку на это.
    val freeSpace : ZNat = (allTheSpace - nonWeightedElementsSpace).refine
    SpacePerWeightUnit(allTheWeight, freeSpace)
  end if
end spacePerWeightForContainerElements

/**
 * Считает суммарный размер всех элементов без веса.
 */
def fixedSpace[T](children : List[MaybeWeighted[Measurable[T]]], constraints: AxisDependentBounds) : ZNat =
  children.map {
    case MaybeWeighted(value, None) =>
      value.placeInside(constraints.bounds).mainAxisValue(constraints.axis)
    case _ => 0
  }.sum.refine // Тут мы уверены, что значение >= 0, так как это сумма ZNat. Просто это нельзя по человечески выразить в типе.
end fixedSpace
