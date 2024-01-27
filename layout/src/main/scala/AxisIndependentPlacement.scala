package me.katze.imagy.layout

import cats.data.State
import cats.syntax.all.{ *, given }
import me.katze
import me.katze.imagy
import me.katze.imagy.components
import me.katze.imagy.components.layout
import me.katze.imagy.components.layout.{ AdditionalAxisStrategy, MainAxisStrategy }

def place[T](sized : List[Sized[T]], constraints: AxisDependentConstraints, strategy: AxisBasedContainerStrategy, axis : Axis) : List[Placed[T]] =
  sized
    .traverse(placeOne(_, strategy, axis))
    .runA(constraints)
    .value
end place

def placeOne[T](sized : Sized[T], strategy: AxisBasedContainerStrategy, axis: Axis) : State[AxisDependentConstraints, Placed[T]] =
  for
    constraints             <- State.get[AxisDependentConstraints]
    mainAxisCoordinate       = placeMainAxis(sized, strategy.mainAxisStrategy, constraints)
    additionalAxisCoordinate = placeAdditionalAxis(sized, strategy.additionalAxisStrategy, constraints)
    result                   = placed(sized, mainAxisCoordinate, additionalAxisCoordinate, axis)
    _                       <- updateStateAccordingResult(result)
  yield result
end placeOne

def updateStateAccordingResult[T](placed: Placed[T]): State[AxisDependentConstraints, Unit] =
  ???
end updateStateAccordingResult

def placeMainAxis[T](sized : Sized[T], strategy: MainAxisStrategy, constraints: AxisDependentConstraints) : Int =
  strategy match
    case MainAxisStrategy.Begin => ???
    case MainAxisStrategy.End => ???
    case MainAxisStrategy.Center => ???
    case MainAxisStrategy.Auto => ???
end placeMainAxis

def placeAdditionalAxis[T](sized : Sized[T], strategy: AdditionalAxisStrategy, constraints: AxisDependentConstraints) : Int =
  strategy match
    case AdditionalAxisStrategy.Begin  => 0
    case AdditionalAxisStrategy.Center => ???
    case AdditionalAxisStrategy.End    => ???
end placeAdditionalAxis


def placed[T](sized: Sized[T], mainAxisCoordinate : Int, additionalAxisCoordinate : Int, axis : Axis) : Placed[T] =
  axis match
    case Axis.Vertical   => Placed(sized.value, additionalAxisCoordinate, mainAxisCoordinate, sized.width, sized.height)
    case Axis.Horizontal => Placed(sized.value, mainAxisCoordinate, additionalAxisCoordinate, sized.width, sized.height)
  end match
end placed
