package me.katze.imagy
package midle

import cats.data.*
import cats.syntax.all.{ *, given }
import ColumnPlaceable.*

import scala.math

class ColumnPlaceable[F[+_], E](
                                 elements: List[Placeable[F[E]]],
                                 override val weight: Option[Int]
                               )(using c : PlacedContainer[F]) extends Placeable[F[E]]:
  override def placeInside(rect: Rect): Placed[F[E]] =
    val placedChildren = placeChildren(rect)
    c.multipleFixedElements(placedChildren, rect)
  end placeInside
  
  private def placeChildren(rect: Rect) : List[Placed[F[E]]] =
    elements
      .traverse(
        placeChild(
          _,
          freeHeight = notFixedHeight(elements, rect),
          allWeight  = weightOf(elements)
        )
      )
      .runA(rect)
      .value
  end placeChildren
end ColumnPlaceable

object ColumnPlaceable:
  def weightOf[T](elements : List[Placeable[T]]) : Int =
    elements.mapFilter(_.weight).sum
  end weightOf
  
  def notFixedHeight[T](elements: List[Placeable[T]], rect: Rect): Int =
    math.max(rect.height - fixedHeight(elements, rect), 0)
  end notFixedHeight
  
  def fixedHeight[T](elements : List[Placeable[T]], rect: Rect): Int =
    elements.map(fixedHeight(_, rect)).sum
  end fixedHeight
  
  def fixedHeight[T](placeable: Placeable[T], rect : Rect) : Int =
    placeable.weight match
      case Some(_) => 0
      case None => placeable.placeInside(rect).height
    end match
  end fixedHeight
  
  def placeChild[T](child: Placeable[T], freeHeight: Int, allWeight: Int): State[Rect, Placed[T]] =
    for
      rect <- State.get[Rect]
      placedChild = child.placeInside(rectFor(child, rect, freeHeight, allWeight))
      _ <- State.modify(cutTopPixels(placedChild.height))
    yield placedChild
  end placeChild
  
  def rectFor[T](child : Placeable[T], initial : Rect, freeHeight: Int, allWeight: Int): Rect =
    child.weight match
      case Some(value) => initial.copy(height = freeHeight * value / allWeight)
      case None        => initial
    end match
  end rectFor
  
  /**
   * Отрезает первые amount пикселей от Rect сверху
   */
  val cutTopPixels : Int => Rect => Rect =
    (amount : Int) => (state: Rect) => state.copy(y = state.y + amount, height = state.height - amount)

