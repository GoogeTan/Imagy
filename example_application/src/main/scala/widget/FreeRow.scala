package me.katze.imagy.example
package widget

import draw.{ Drawable, LayoutWidget }
import update.EventConsumer

import cats.{ Applicative, Monoid }
import io.github.iltotore.iron.{ *, given }
import me.katze.imagy.components.layout.MaybeWeighted
import me.katze.imagy.components.layout.strategy.AdditionalAxisStrategy
import me.katze.imagy.layout.bound.Bounds
import me.katze.imagy.layout.constraint.given
import me.katze.imagy.layout.rowcolumn.weightedRow
import me.katze.imagy.layout.{ Layout, Measurable, Placed, Sized }

type PlacedWidget[F[+_], +G, +FreeWidget, -DownEvent, +UpEvent] = Drawable[G] & EventConsumer[FreeWidget, F, DownEvent, UpEvent]

final case class FreeRow[
  F[+_] : Applicative, G : Monoid, FreeWidget, DownEvent, UpEvent
](
    children : List[MaybeWeighted[Measurable[PlacedWidget[F, G, FreeWidget, DownEvent, UpEvent]]]],
    horizontalStrategy : AdditionalAxisStrategy
) extends Measurable[PlacedWidget[F, G, FreeWidget, DownEvent, UpEvent]]:
  
  given Layout[PlacedWidget[F, G, FreeWidget, DownEvent, UpEvent]] with
    override def apply(elements: List[Placed[PlacedWidget[F, G, FreeWidget, DownEvent, UpEvent]]]): Sized[PlacedWidget[F, G, FreeWidget, DownEvent, UpEvent]] =
      val width =
        val elem = elements.maxBy(x => x.x + x.width)
        elem.x + elem.width
      end width
      
      val height =
        val elem = elements.maxBy(x => x.y + x.height)
        elem.y + elem.height
      end height
      
      Sized(LayoutWidget[F, G, FreeWidget, DownEvent, UpEvent](???, elements), width, height)
    end apply
  end given
  
  override def placeInside(bounds: Bounds): Sized[PlacedWidget[F, G, FreeWidget, DownEvent, UpEvent]] =
    weightedRow(children.refine, horizontalStrategy).placeInside(bounds)
  end placeInside
end FreeRow  
