package me.katze.imagy.layout

import cats.data.*
import cats.syntax.all.{ *, given }
import me.katze.imagy.common.{ Nat, ZNat }
import io.github.iltotore.iron.{ *, given }
import me.katze.imagy.components.layout.{ AdditionalAxisStrategy, MainAxisStrategy, MaybeWeighted }

import scala.math.max

final class WeightedColumnMeasurable[T](
                                          children           : List[MaybeWeighted[Measurable[T]]],
                                          layout             : Layout[T],
                                          verticalStrategy   : MainAxisStrategy = MainAxisStrategy.Begin,
                                          horizontalStrategy : AdditionalAxisStrategy = AdditionalAxisStrategy.Center,
                                        ) extends Measurable[T]:
  private val fixedChildren: List[Measurable[T]] = children.flatMap:
    case MaybeWeighted.Weighted(_, _) => Nil
    case MaybeWeighted.Const(a) => List(a)
  end fixedChildren
  
  override def placeInside(constrains: Constraints): Sized[T] =
    layout(
      placeAllTheChildren(
        children,
        heightFor(fixedChildren, constrains),
        constrains
      )
    )
  end placeInside
end WeightedColumnMeasurable

def placeAllTheChildren[T](
                            children: List[MaybeWeighted[Measurable[T]]],
                            fixedHeight: ZNat,
                            constraints: Constraints,
                            verticalStrategy: MainAxisStrategy = MainAxisStrategy.Begin,
                            horizontalStrategy: AdditionalAxisStrategy = AdditionalAxisStrategy.Center
                          ): List[Placed[T]] =
  val allTheWeight: ZNat = allTheWeightFor(children)
  assert(constraints.hasFixedHeight || allTheWeight == 0)
  // Если мы сюда попали, то у нас либо есть ограничения, либо это значение никогда не будет использовано. Так что всё хорошо, даже если там Inf.
  val freeHeight: ZNat = max(constraints.maxHeight - fixedHeight, 0).refine
  val measured = measureChildren(children, freeHeight, allTheWeight, constraints)
  val placed = placeChildren(measured, constraints, verticalStrategy, horizontalStrategy)
  placed
end placeAllTheChildren

def placeChildren[T](
                      children: List[Sized[T]],
                      constraints: Constraints,
                      verticalStrategy: MainAxisStrategy = MainAxisStrategy.Begin,
                      horizontalStrategy: AdditionalAxisStrategy = AdditionalAxisStrategy.Center,
                    ): List[Placed[T]] =
  placeHorizontally(children, constraints, horizontalStrategy)
    .zip(placeVertically(children, constraints, verticalStrategy))
    .map((x, y) =>
      assert(x.value == y.value)
      assert(x.width == y.width)
      assert(x.height == y.height)
      Placed(x.value, x.x, y.y, x.width, y.height)
    )
end placeChildren

def placeVertically[T](
                        children : List[Sized[T]],
                        constraints: Constraints,
                        verticalStrategy   : MainAxisStrategy = MainAxisStrategy.Begin,
                      ) : List[Placed[T]] =
  ???
end placeVertically

def placeHorizontally[T](
                        children: List[Sized[T]],
                        constraints: Constraints,
                        horizontalStrategy: AdditionalAxisStrategy = AdditionalAxisStrategy.Begin,
                      ): List[Placed[T]] =
  ???
end placeHorizontally

def measureChildren[T](children: List[MaybeWeighted[Measurable[T]]], freeHeight : ZNat, allTheWeight : ZNat, constraints: Constraints): List[Sized[T]] =
  children
    .traverse(measureChildS(_, freeHeight, allTheWeight))
    .runA(constraints)
    .value
end measureChildren

/**
 * TODO Написать документацию. По типу не понятно, что тут происходит.
 * @return
 */
def measureChildS[T](child: MaybeWeighted[Measurable[T]], freeHeight: ZNat, allTheWeight: ZNat): State[Constraints, Sized[T]] =
  State:
    (constrains: Constraints) =>
      val result = measureChild(child, constrains, freeHeight, allTheWeight)
      (recalculateConstraints(result, constrains), result)
end measureChildS

def recalculateConstraints[T](placed : Sized[T], constraints: Constraints) : Constraints = ???

def measureChild[T](child : MaybeWeighted[Measurable[T]], constraints: Constraints, freeHeight: ZNat, allTheWeight: ZNat) : Sized[T] =
  child match
    case MaybeWeighted.Weighted(measurable, weight) =>
      assert(allTheWeight != 0, "Sum weight must be greater then 0 when any weighted exists")
      val height = calculateWeightedHeight(freeHeight, allTheWeight.refine, weight)
      val result = measurable.placeInside(constraints.withMaxHeight(height))
      result
    case MaybeWeighted.Const(measurable) =>
      measurable.placeInside(constraints)
  end match
end measureChild

def calculateWeightedHeight(freeHeight: ZNat, allTheWeight: Nat, weight: Nat) : ZNat =
  if freeHeight == Constraints.Infinity then
    Constraints.Infinity
  else if freeHeight > Int.MaxValue / weight then // На случай переполнения. Вряд ли достижимо, но лишним не будет.
    (freeHeight / allTheWeight * weight).refine
  else
    (weight * freeHeight / allTheWeight).refine
  end if
end calculateWeightedHeight

def allTheWeightFor[T](children : List[MaybeWeighted[T]]) : ZNat =
  children.map {
    case MaybeWeighted.Weighted(_, weight) => weight
    case _ => 0
  }.sum.refine
end allTheWeightFor

def heightFor[T](children : List[Measurable[T]], constraints: Constraints) : ZNat =
  children
    .map(_.placeInside(constraints))
    .foldLeft(0)(_ + _.height)
    .refine
end heightFor
