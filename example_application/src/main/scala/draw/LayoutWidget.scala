package me.katze.imagy.example
package draw

import update.{ EventConsumer, EventProcessResult }
import widget.PlacedWidget

import cats.*
import cats.syntax.all.{ *, given }
import me.katze.imagy.layout.Placed

/**
 * TODO написать доку.
 * @param freeLayout row, column или что-бы то ни было констурктор
 */
final case class LayoutWidget[
  F[+_] : Applicative, +G : Monoid, FreeWidget, -DownEvent, +UpEvent
](
    freeLayout : List[FreeWidget] => FreeWidget,
    children : List[Placed[PlacedWidget[F, G, FreeWidget, DownEvent, UpEvent]]]
) extends Drawable[G] with EventConsumer[FreeWidget, F, DownEvent, UpEvent]:
  // TODO отсровка в соответствии с местом
  override def draw: G =
    children.foldMap(_.value.draw)
  end draw
  
  override def processEvent(event: DownEvent): F[EventProcessResult[FreeWidget, UpEvent]] =
    children
      .traverse(child => child.value.processEvent(event))
      .map(results =>
        EventProcessResult(
          freeLayout(results.map(_.value)),
          results.flatMap(_.events)
        )
      )
  end processEvent
end LayoutWidget
