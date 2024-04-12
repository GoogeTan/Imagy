package me.katze.imagy.example
package widget

import draw.Drawable
import place.ApplicationBounds
import update.{ EventConsumer, EventProcessResult }

import cats.implicits.{ *, given }
import cats.syntax.all.{ *, given }
import cats.syntax.foldable.given
import cats.{ *, given }
import me.katze.imagy.layout.Measurable

final case class RunnableIO[+F[_], A](io : F[A], path : Path, keepAliveAfterWidgetDetach : Boolean)
final case class EventResult[+F[_], +Widget, +UpEvent](widget : Widget, events : List[UpEvent], ios : List[RunnableIO[F, ?]])

trait TruePlacedWidget[F[+_], +G, -DownEvent, +UpEvent] extends Drawable[G]:
  type FreeWidget <: Measurable[TruePlacedWidget[F, G, DownEvent, UpEvent]]
  
  def processEvent(downEvent : DownEvent, path : Path) : EventResult[F, FreeWidget, UpEvent]
  
  def deadOnes(paths : Set[Path]) : Set[Path]
end TruePlacedWidget


final class RootWidgetPlaced[F[+_] : Monad : ApplicationBounds, +G, -DownEvent, +UpEvent](
  widget : TruePlacedWidget[F, G, DownEvent, UpEvent],
  master : IOMaster[F]
) extends Drawable[G] with EventConsumer[RootWidgetFree[F, G, DownEvent, UpEvent], F, DownEvent, UpEvent]:
  
  override def processEvent(event: DownEvent): F[EventProcessResult[RootWidgetFree[F, G, DownEvent, UpEvent], UpEvent]] =
    val res = widget.processEvent(event, Path(List("root")))
    pushIOS(res.ios) *> EventProcessResult(RootWidgetFree(res.widget, master), res.events).pure[F]
  end processEvent
  
  private def pushIOS(ios : List[RunnableIO[F, ?]]) : F[Unit] =
    ios.traverse_(io => master.pushIO(io.io, io.path, io.keepAliveAfterWidgetDetach))
  end pushIOS
  
  override def draw: G = widget.draw
end RootWidgetPlaced


final class RootWidgetFree[
  F[+_] : Monad, +G, -DownEvent, +UpEvent
](
  measurable: Measurable[TruePlacedWidget[F, G, DownEvent, UpEvent]],
  master : IOMaster[F]
)(
  using bounds : ApplicationBounds[F]
) extends Placeable[F, RootWidgetPlaced[F, G, DownEvent, UpEvent]]:
  override def place(): F[RootWidgetPlaced[F, G, DownEvent, UpEvent]] =
    for
      bounds <- bounds.currentBounds
      placed =  measurable.placeInside(bounds).value
      _ <- killDeadIOS(placed)
    yield RootWidgetPlaced(placed, master)
  end place
  
  private def killDeadIOS(newWidget : TruePlacedWidget[F, ?, ?, ?]) : F[Unit] =
    for
      alive  <- master.alive
      dead   =  newWidget.deadOnes(alive)
      _      <- dead.toList.traverse_(master.detach)
    yield ()
  end killDeadIOS
end RootWidgetFree
