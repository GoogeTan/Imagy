package me.katze.imagy.example

import draw.Drawable
import place.ApplicationBounds
import update.{ EventConsumer, EventProcessResult }

import cats.*
import cats.syntax.all.{ *, given }
import me.katze.imagy.layout.{ Measurable, Placed, Sized }

/**
 * TODO написать норм доку.
 * @param placed
 * @tparam F
 * @tparam G
 * @tparam DownEvent
 * @tparam UpEvent
 * @tparam FreeWidget
 * @tparam PlacedWidget
 */
final case class PlaceableAdapter[
  +F[+_] : Monad : ApplicationBounds,
  +G,
  -DownEvent,
  +UpEvent,
  +FreeWidget <: Measurable[PlacedWidget],
  +PlacedWidget <: Drawable[G] & EventConsumer[FreeWidget, F, DownEvent, UpEvent]
](
    placed : Sized[PlacedWidget]
) extends Drawable[G] with EventConsumer[PlaceableAdapter[F, G, DownEvent, UpEvent, FreeWidget, PlacedWidget], F, DownEvent, UpEvent]:
  override def draw: G = placed.value.draw
  
  override def processEvent(event: DownEvent): F[EventProcessResult[PlaceableAdapter[F, G, DownEvent, UpEvent, FreeWidget, PlacedWidget], UpEvent]] =
    for
      result <- placed.value.processEvent(event)
      bounds <- summon[ApplicationBounds[F]].currentBounds
      newPlacedWidget = result.freeWidget.placeInside(bounds)
    yield EventProcessResult(PlaceableAdapter(newPlacedWidget), result.events)
  end processEvent
end PlaceableAdapter
