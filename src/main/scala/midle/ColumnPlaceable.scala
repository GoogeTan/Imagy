package me.katze.imagy
package midle

import common.{ Nat, ZNat }
import midle.ColumnPlaceable.*

import cats.data.*
import cats.syntax.all.{ *, given }
import io.github.iltotore.iron.constraint.numeric.Greater
import io.github.iltotore.iron.{ *, given }

import scala.math

class ColumnPlaceable[E](
                          children: List[Placeable[E]],
                          override val weight: Option[Nat],
                          multipleFixedElements : (List[Placed[E]], Rect) => Placed[E] // TODO better name
                        ) extends Placeable[E]:
  override def placeInside(rect: Rect): Placed[E] =
    val placedChildren = placeChildren(rect)
    multipleFixedElements(placedChildren, rect)
  end placeInside
  
  private def placeChildren(rect: Rect) : List[Placed[E]] =
    val notFixedH = notFixedHeight(children, rect)
    val allWeight = weightOf(children)
    children
      .traverse(
        placeChild(
          _,
          freeHeight = notFixedH,
          allWeight  = allWeight
        )
      )
      .runA(rect)
      .value
  end placeChildren
end ColumnPlaceable

object ColumnPlaceable:
  def weightOf[T](elements : List[Placeable[T]]) : ZNat =
    elements.mapFilter(_.weight).sum.refine
  end weightOf
  
  def notFixedHeight[T](elements: List[Placeable[T]], rect: Rect): ZNat =
    math.max(rect.height - fixedHeight(elements, rect), 0).refine
  end notFixedHeight
  
  // TODO better name
  def fixedHeight[T](elements : List[Placeable[T]], rect: Rect): ZNat =
    elements.map(fixedHeight(_, rect)).sum.refine
  end fixedHeight
  
  // TODO better name
  def fixedHeight[T](placeable: Placeable[T], rect : Rect) : ZNat =
    placeable.weight match
      case Some(_) => 0
      case None => placeable.placeInside(rect).height
    end match
  end fixedHeight
  
  def placeChild[T](child: Placeable[T], freeHeight: ZNat, allWeight: ZNat): State[Rect, Placed[T]] =
    for
      rect <- State.get[Rect]
      placedChild = child.placeInside(rectFor(child, rect, freeHeight, allWeight))
      _ <- State.modify(cutTopPixels(placedChild.height))
    yield placedChild
  end placeChild
  
  // TODO better name
  def rectFor[T](child : Placeable[T], initial : Rect, freeHeight: ZNat, allWeight: ZNat): Rect =
    child.weight match
      case Some(value) =>
        assert(allWeight != 0, "If one element has weight, sum of the weights must be positive. Create an issue")
        initial.copy(height = (freeHeight * value / allWeight).refine)
      case _ => initial
    end match
  end rectFor
  
  /**
   * Отрезает первые amount пикселей от Rect сверху. Если не хватает, то останется 0.
   * TODO better name
   */
  val cutTopPixels : Int => Rect => Rect =
    (amount : Int) => (state: Rect) => state.copy(height = math.max(state.height - amount, 0).refine)

