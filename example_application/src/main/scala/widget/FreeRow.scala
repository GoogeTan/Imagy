package me.katze.imagy.example
package widget

import draw.Drawable
import update.EventConsumer

import cats.{ Applicative, Monad, Monoid }
import cats.syntax.all.{ *, given }
import io.github.iltotore.iron.{ *, given }
import me.katze.imagy.common.{ Nat, ZNat }
import me.katze.imagy.components.layout.{ *, given }
import me.katze.imagy.components.layout.strategy.AdditionalAxisStrategy
import me.katze.imagy.layout.bound.Bounds
import me.katze.imagy.layout.constraint.given
import me.katze.imagy.layout.rowcolumn.weightedRow
import me.katze.imagy.layout.{ Layout, Measurable, Placed, Sized }

final case class FreeRow[
  F[+_] : Applicative, G : Monoid, DownEvent, UpEvent
](
    children : List[MaybeWeighted[Measurable[TruePlacedWidget[F, G, DownEvent, UpEvent]]]],
    horizontalStrategy : AdditionalAxisStrategy
) extends Measurable[TruePlacedWidget[F, G, DownEvent, UpEvent]]:
  
  given Layout[RowElementPlaced[F, G, DownEvent, UpEvent]] with
    override def apply(elements: List[Placed[RowElementPlaced[F, G, DownEvent, UpEvent]]]): Sized[TruePlacedWidget[F, G, DownEvent, UpEvent]] =
      val width : ZNat =
        val elem = elements.maxBy(x => x.x + x.width)
        (elem.x + elem.width).refine
      end width
      
      val height : ZNat =
        val elem = elements.maxBy(x => x.y + x.height)
        (elem.y + elem.height).refine
      end height
      val weights = elements.map(_.value.weight)
      Sized(
        PlacedLayout[F, G, DownEvent, UpEvent, RowElementFree[F, G, DownEvent, UpEvent], RowElementPlaced[F, G, DownEvent, UpEvent]](
          elements,
          (newChildren) => FreeRow[F, G, DownEvent, UpEvent](newChildren.zip(weights).map(MaybeWeighted.apply[RowElementFree[F, G, DownEvent, UpEvent]]), horizontalStrategy)
        ),
        width,
        height
      )
    end apply
  end given
  
  override def placeInside(bounds: Bounds): Sized[TruePlacedWidget[F, G, DownEvent, UpEvent]] =
    weightedRow(children.refine, horizontalStrategy).placeInside(bounds)
  end placeInside
end FreeRow

final class RowElementFree[F[+_], G, DownEvent, UpEvent] extends Measurable[TruePlacedWidget[F, G, DownEvent, UpEvent]]:
  override def placeInside(constrains: Bounds): Sized[TruePlacedWidget[F, G, DownEvent, UpEvent]] = ???
  
end RowElementFree

final class RowElementPlaced[F[+_], G, -DownEvent, +UpEvent](
                                                            val weight : Option[Nat]
                                                          ) extends TruePlacedWidget[F, G, DownEvent, UpEvent]:
  override type FreeWidget = RowElementFree[F, G, DownEvent, UpEvent]
  
  override def deadOnes(paths: Set[Path]): Set[Path] = ???
  
  override def processEvent(downEvent: DownEvent, path: Path): EventResult[F, FreeWidget, UpEvent] = ???
  
  override def draw: G = ???
  
end RowElementPlaced


final class PlacedLayout[
  F[+_],
  G : Monoid,
  DownEvent,
  UpEvent,
  ElementFreeWidget,
  ElementPlacedWidget <: TruePlacedWidget[F, G, DownEvent, UpEvent] {
    type FreeWidget = ElementFreeWidget
  }
](
    elements: List[Placed[ElementPlacedWidget]],
    freeWidget : List[ElementFreeWidget] => Measurable[TruePlacedWidget[F, G, DownEvent, UpEvent]]
) extends TruePlacedWidget[F, G, DownEvent, UpEvent]:
  override type FreeWidget = Measurable[TruePlacedWidget[F, G, DownEvent, UpEvent]]
  
  override def deadOnes(paths: Set[Path]): Set[Path] = elements.foldRight(paths)((a, b) => a.value.deadOnes(b))
  
  override def processEvent(downEvent: DownEvent, path: Path): EventResult[F, Measurable[TruePlacedWidget[F, G, DownEvent, UpEvent]], UpEvent] =
    val processResults = elements.map(_.value.processEvent(downEvent, path))
    
    val children = processResults.map(_.widget)
    val events = processResults.flatMap(_.events)
    val ios = processResults.flatMap(_.ios)
    
    EventResult(freeWidget(children), events, ios)
  end processEvent
  
  // TODO сделать сдвиги на координаты
  override def draw: G = elements.foldMap(_.value.draw)
end PlacedLayout