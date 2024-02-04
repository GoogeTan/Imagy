package me.katze.imagy.layout
package rowcolumn

import bound.AxisDependentBounds

import cats.data.State
import cats.syntax.all.{ *, given }
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
                        bounds: AxisDependentBounds
                      ) : List[Placed[T]] =
  elements
    .traverse(placeOne(_, mainAxisStrategy, additionalAxisStrategy, bounds, elements.map(_.mainAxisValue(bounds.axis)).sum, elements.size))
    .runA(RowColumnPlacementState(0))
    .value
end rowColumnPlace

final case class RowColumnPlacementState(alreadyPlaced : ZNat)

def placeOne[T](
                  element : Sized[T],
                  mainAxisStrategy: MainAxisStrategy,
                  additionalAxisStrategy: AdditionalAxisStrategy,
                  bounds: AxisDependentBounds,
                  allTheSize : Int,
                  count : Int
                ) : State[RowColumnPlacementState, Placed[T]] =
  for
    state                   <- State.get[RowColumnPlacementState]
    mainAxisCoordinate       = placeMainAxis(element, mainAxisStrategy, state, bounds, allTheSize)
    additionalAxisCoordinate = placeAdditionalAxis(element, additionalAxisStrategy, bounds)
    result                   = placed(element, mainAxisCoordinate, additionalAxisCoordinate, bounds.axis)
    _                       <- updateStateAccordingResult(result, bounds.axis, (bounds.mainAxisMaxValue.get - allTheSize) / count)
  yield result
end placeOne

def updateStateAccordingResult[T](
                                    placed: Placed[T], mainAxis : Axis,
                                    spaceBetween : Int
                                  ): State[RowColumnPlacementState, Unit] =
  State.modify:
    state => state.copy(alreadyPlaced = (state.alreadyPlaced + placed.axisValue(mainAxis) + spaceBetween).refine)
end updateStateAccordingResult

def placeMainAxis[T](
                      sized: Sized[T],
                      mainAxisStrategy: MainAxisStrategy,
                      state: RowColumnPlacementState,
                      bounds: AxisDependentBounds,
                      allTheSize : Int
                    ): Int =
  def mainAxisMaxValue(bounds: AxisDependentBounds): ZNat =
    bounds.mainAxisMaxValue.getOrElse(
      throw new IllegalStateException(s"Main axis size have infinite size and $mainAxisStrategy placement strategy.")
    )
  end mainAxisMaxValue
  
  mainAxisStrategy match
    case Begin | SpaceBetween =>
      state.alreadyPlaced
    case Center =>
      val start = (mainAxisMaxValue(bounds) - allTheSize) / 2
      state.alreadyPlaced + start
    case End =>
      val start = mainAxisMaxValue(bounds) - allTheSize
      state.alreadyPlaced + start
  end match
end placeMainAxis

def placeAdditionalAxis[T](sized : Sized[T], additionalAxisStrategy: AdditionalAxisStrategy, bounds: AxisDependentBounds) : Int =
  def additionalAxisMaxValue(bounds: AxisDependentBounds, strategy: AdditionalAxisStrategy) : ZNat =
    bounds.additionalAxisMaxValue.getOrElse(
      throw new IllegalStateException(s"Additional axis size have infinite size and $strategy placement strategy.")
    )
  end additionalAxisMaxValue

  additionalAxisStrategy match
    case Begin  => 0
    case Center => (additionalAxisMaxValue(bounds, additionalAxisStrategy) - sized.mainAxisValue(bounds.axis)) / 2
    case End    =>  additionalAxisMaxValue(bounds, additionalAxisStrategy) - sized.mainAxisValue(bounds.axis)
  end match
end placeAdditionalAxis

def placed[T](sized: Sized[T], mainAxisCoordinate : Int, additionalAxisCoordinate : Int, axis : Axis) : Placed[T] =
  axis match
    case Axis.Vertical   => Placed(sized.value, additionalAxisCoordinate, mainAxisCoordinate, sized.width, sized.height)
    case Axis.Horizontal => Placed(sized.value, mainAxisCoordinate, additionalAxisCoordinate, sized.width, sized.height)
  end match
end placed
