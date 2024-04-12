package me.katze.imagy.layout
package rowcolumn

import bound.AxisDependentBounds
import bound.constraints.{ AdditionalAxisConstraint, MainAxisConstraint, StrategyBasedFiniteness }

import cats.data.State
import cats.syntax.all.{ *, given }
import io.github.iltotore.iron.constraint.all.GreaterEqual
import io.github.iltotore.iron.constraint.numeric.Greater
import io.github.iltotore.iron.{ *, given }
import me.katze
import me.katze.imagy
import me.katze.imagy.common.ZNat
import me.katze.imagy.components
import me.katze.imagy.components.layout
import me.katze.imagy.components.layout.strategy.*

def rowColumnPlace[T](
                        elements : List[Sized[T]],
                        mainAxisStrategy: MainAxisStrategy,
                        additionalAxisStrategy: AdditionalAxisStrategy,
                        bounds: AxisDependentBounds :| (MainAxisConstraint[StrategyBasedFiniteness[mainAxisStrategy.type]] & AdditionalAxisConstraint[StrategyBasedFiniteness[additionalAxisStrategy.type]]),
                      ) : List[Placed[T]] =
  val allTheSum: ZNat = elements.map(_.mainAxisValue(bounds.axis)).sum.refine
  val size: ZNat = elements.size.refine
  elements
    .traverse(placeOne(_, mainAxisStrategy, additionalAxisStrategy, bounds, allTheSum, size))
    .runA(RowColumnPlacementState(0))
    .value
end rowColumnPlace

final case class RowColumnPlacementState(alreadyPlaced : ZNat)

def placeOne[T](
                  element : Sized[T],
                  mainAxisStrategy: MainAxisStrategy,
                  additionalAxisStrategy: AdditionalAxisStrategy,
                  bounds: AxisDependentBounds :| (MainAxisConstraint[StrategyBasedFiniteness[mainAxisStrategy.type]] & AdditionalAxisConstraint[StrategyBasedFiniteness[additionalAxisStrategy.type]]),
                  allTheSize : ZNat,
                  count : ZNat
                ) : State[RowColumnPlacementState, Placed[T]] =
  for
    state                   <- State.get[RowColumnPlacementState]
    mainAxisCoordinate       = placeMainAxis(mainAxisStrategy, state, bounds, allTheSize)
    additionalAxisCoordinate = placeAdditionalAxis(element, additionalAxisStrategy, bounds)
    result                   = placed(element, mainAxisCoordinate, additionalAxisCoordinate, bounds.axis)
    spaceBetween             = bounds.mainAxisMaxValue.map(_ - allTheSize).map(_ / count).flatMap(_.refineOption[GreaterEqual[0]])
    _                       <- updateStateAccordingResult(result, bounds.axis, spaceBetween)
  yield result
end placeOne

def updateStateAccordingResult[T](
                                    placed: Placed[T],
                                    mainAxis : Axis,
                                    spaceBetween : Option[ZNat]
                                  ): State[RowColumnPlacementState, Unit] =
  State.modify:
    state => state.copy(alreadyPlaced = (state.alreadyPlaced + placed.axisValue(mainAxis) + spaceBetween.getOrElse(0)).refine)
end updateStateAccordingResult

def placeMainAxis(
                    mainAxisStrategy: MainAxisStrategy,
                    state: RowColumnPlacementState,
                    bounds: AxisDependentBounds :| MainAxisConstraint[StrategyBasedFiniteness[mainAxisStrategy.type]],
                    allTheSize : ZNat
                  ): ZNat =
  
  mainAxisStrategy match
    case Begin | SpaceBetween =>
      state.alreadyPlaced
    case Center =>
      // Мы пропаттернматчили стратению и уверены, что StrategyBasedFiniteness[Center.type] - Finite.
      val start = (MainAxisConstraint.mainAxisValue(bounds.assume) - allTheSize) / 2
      (state.alreadyPlaced + start).refine // TODO подумать, как кинуть адекватную ошибку заранее, чтобы не вылететь тут
    case End =>
      // Мы пропаттернматчили стратению и уверены, что StrategyBasedFiniteness[End.type] - Finite.
      val start = MainAxisConstraint.mainAxisValue(bounds.assume) - allTheSize
      (state.alreadyPlaced + start).refine // TODO
  end match
end placeMainAxis

def placeAdditionalAxis[T](
                            sized : Sized[T],
                            additionalAxisStrategy: AdditionalAxisStrategy, 
                            bounds: AxisDependentBounds :| AdditionalAxisConstraint[StrategyBasedFiniteness[additionalAxisStrategy.type]]
                          ) : ZNat =
  def additionalAxisMaxValue(bounds: AxisDependentBounds, strategy: AdditionalAxisStrategy) : ZNat =
    bounds.additionalAxisMaxValue.getOrElse(
      throw IllegalArgumentException(s"Additional axis size have infinite size and $strategy placement strategy.")
    )
  end additionalAxisMaxValue

  additionalAxisStrategy match
    case Begin  => 0
    case Center => ((additionalAxisMaxValue(bounds, additionalAxisStrategy) - sized.mainAxisValue(bounds.axis)) / 2).refine
    case End    =>  (additionalAxisMaxValue(bounds, additionalAxisStrategy) - sized.mainAxisValue(bounds.axis)).refine
  end match
end placeAdditionalAxis

def placed[T](sized: Sized[T], mainAxisCoordinate : ZNat, additionalAxisCoordinate : ZNat, axis : Axis) : Placed[T] =
  axis match
    case Axis.Vertical   => Placed(sized.value, additionalAxisCoordinate, mainAxisCoordinate, sized.width, sized.height)
    case Axis.Horizontal => Placed(sized.value, mainAxisCoordinate, additionalAxisCoordinate, sized.width, sized.height)
  end match
end placed
