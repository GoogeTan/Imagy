package me.katze.imagy.layout

import bound.AxisDependentBounds
import bound.constraints.MainAxisConstraint
import unit.MeasurementUnit
import unit.constraints.Finite

import cats.data.State
import cats.syntax.all.{ *, given }
import io.github.iltotore.iron.{ :|, given }
import me.katze
import me.katze.imagy
import me.katze.imagy.common.ZNat
import me.katze.imagy.components
import me.katze.imagy.components.layout
import me.katze.imagy.components.layout.strategy.{ AdditionalAxisStrategy, Begin, Center, End }

def place[T](
              sized : List[Sized[T]],
              strategy: AdditionalAxisStrategy,
              bounds: AxisDependentBounds :| MainAxisConstraint[Finite]
            ) : List[Placed[T]] =
  sized
    .traverse(placeOne(_, strategy, bounds.axis))
    .runA(PlacementState(0, bounds))
    .value
end place

final case class PlacementState(alreadyPlaced : ZNat, bounds: AxisDependentBounds :| MainAxisConstraint[Finite])

def placeOne[T](sized : Sized[T], additionalAxisStrategy: AdditionalAxisStrategy, axis: Axis) : State[PlacementState, Placed[T]] =
  for
    state                   <- State.get[PlacementState]
    mainAxisCoordinate       = state.alreadyPlaced
    additionalAxisCoordinate = placeAdditionalAxis(sized, additionalAxisStrategy)(state)
    result                   = placed(sized, mainAxisCoordinate, additionalAxisCoordinate, axis)
    _                       <- updateStateAccordingResult(result)
  yield result
end placeOne

def updateStateAccordingResult[T](placed: Placed[T]): State[PlacementState, Unit] =
  ???
end updateStateAccordingResult

def placeAdditionalAxis[T](sized : Sized[T], strategy: AdditionalAxisStrategy)(state: PlacementState) : Int =
  strategy match
    case Begin  =>
      state.alreadyPlaced
    case Center =>
      val bounds = state.bounds
      val start = (mainAxisMaxValue(state) - sized.mainAxisValue(bounds.axis)) / 2
      state.alreadyPlaced + start
    case End    =>
      val bounds = state.bounds
      val start = mainAxisMaxValue(state) - sized.mainAxisValue(bounds.axis)
      state.alreadyPlaced + start
  end match
end placeAdditionalAxis

def mainAxisMaxValue(state: PlacementState) : ZNat =
  state.bounds.mainAxis.max  match
    case MeasurementUnit.Infinite => throw new IllegalStateException("Impossible! Create an issue in case this happened")
    case MeasurementUnit.Value(value) => value
  end match
end mainAxisMaxValue

def placed[T](sized: Sized[T], mainAxisCoordinate : Int, additionalAxisCoordinate : Int, axis : Axis) : Placed[T] =
  axis match
    case Axis.Vertical   => Placed(sized.value, additionalAxisCoordinate, mainAxisCoordinate, sized.width, sized.height)
    case Axis.Horizontal => Placed(sized.value, mainAxisCoordinate, additionalAxisCoordinate, sized.width, sized.height)
  end match
end placed
