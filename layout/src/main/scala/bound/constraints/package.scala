package me.katze.imagy.layout
package bound

import bound.constraints.{ AdditionalAxisConstraint, MainAxisConstraint }
import unit.MeasurementUnit
import unit.constraints.{ Finite, Infinite }

import io.github.iltotore.iron.{ Constraint, Pure }
import me.katze.imagy.components.layout.strategy.*

import scala.compiletime.summonInline

package object constraints:

  inline given mainAxisConstraint[C, Impl <: Constraint[AxisBounds, C]](using Impl): Constraint[AxisDependentBounds, MainAxisConstraint[C]] with
    override inline def test(value: AxisDependentBounds): Boolean =
      summonInline[Impl].test(value.mainAxis)
    end test
    
    override inline def message: String = s"Main axis must: ${summonInline[Impl].message}"
  end mainAxisConstraint


  inline given additionalAxisConstraint[C, Impl <: Constraint[AxisBounds, C]](using Impl): Constraint[AxisDependentBounds, AdditionalAxisConstraint[C]] with
    override inline def test(value: AxisDependentBounds): Boolean =
      summonInline[Impl].test(value.additionalAxis)
    end test
    
    override inline def message: String = s"Main axis must: ${summonInline[Impl].message}"
  end additionalAxisConstraint
  
  given [T <: Axis, C](using c: Constraint[AxisBounds, C]): Constraint[Bounds, AxisConstraint[T, C]] with
    override inline def test(value: Bounds): Boolean =
      compiletime.constValue[T] match
        case Axis.Vertical => c.test(value.vertical)
        case Axis.Horizontal => c.test(value.horizontal)
      end match
    end test
    
    override inline def message: String = s"${compiletime.constValue[T]} axis must: ${c.message}"
  end given
  
  
  inline given independentConstraints[T, Impl <: Constraint[Bounds, T]](using Impl): Constraint[AxisDependentBounds, IndependentConstraint[T]] with
    override inline def test(value: AxisDependentBounds): Boolean = summonInline[Impl].test(value.bounds)
    
    override inline def message: String = "TODO MESSAGE"
  end independentConstraints
  
  
  type StrategyBasedFiniteness[T <: MainAxisStrategy] = T match
    case Begin.type => Pure
    case Center.type => Finite
    case End.type => Finite
    case SpaceBetween.type => Finite
  
  given strategyBasedFiniteness[A, T <: MainAxisStrategy, Impl <: Constraint[A, Finite]](using v : T, finite : Impl) : Constraint[A, StrategyBasedFiniteness[T]] with
    override inline def test(value: A): Boolean = v match
      case Begin => true
      case Center| End | SpaceBetween => finite.test(value)
    end test
    
    override inline def message: String = finite.message
  end strategyBasedFiniteness
    
  given axisBoundsFiniteConstraint: Constraint[AxisBounds, Finite] with
    override inline def test(value: AxisBounds): Boolean = value.max != MeasurementUnit.Infinite
    
    override inline def message: String = "AxisBounds must have fixed maximum size."
  end axisBoundsFiniteConstraint
  
  
  given axisBoundsInfiniteConstraint: Constraint[AxisBounds, Infinite] with
    override inline def test(value: AxisBounds): Boolean = value.max == MeasurementUnit.Infinite
    
    override inline def message: String = "AxisBounds must have infinite maximum size"
  end axisBoundsInfiniteConstraint
end constraints
