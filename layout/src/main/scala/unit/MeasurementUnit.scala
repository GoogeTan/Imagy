package me.katze.imagy.layout
package unit

import io.github.iltotore.iron.constraint.all.*
import io.github.iltotore.iron.{ *, given }
import me.katze.imagy.common.ZNat

enum MeasurementUnit extends Ordered[MeasurementUnit]:
  case Infinite
  case Value(value : ZNat)
  
  def valueOption : Option[ZNat] = this match
    case MeasurementUnit.Infinite => None
    case MeasurementUnit.Value(value) => Some(value)
  end valueOption
  
  override def compare(that: MeasurementUnit): Int =
    (this, that) match
      case (Infinite, Infinite) => 0
      case (Infinite, Value(_)) => 1
      case (Value(_), Infinite) => -1
      case (Value(a), Value(b)) =>
        val aa : Int = a // Не знаю почему, но оно не хочет просто так привести типы в выражении. Приходится делать так.
        val bb : Int = b
        aa.compare(bb)
    end match
  end compare
        
  def +(that : MeasurementUnit) : MeasurementUnit =
    (this, that) match
      case (Infinite, _) => Infinite
      case (_, Infinite) => Infinite
      case (Value(a), Value(b)) => MeasurementUnit.Value((a + b).refine[GreaterEqual[0]])
    end match
  end +
  
  def -(that : Int) : Option[MeasurementUnit] =
    this match
      case MeasurementUnit.Infinite => Some(Infinite)
      case MeasurementUnit.Value(value) => (value - that).refineOption[GreaterEqual[0]].map(MeasurementUnit.Value.apply)
    end match
  end -
  
  override def toString: String = this match
    case MeasurementUnit.Infinite => "Inf"
    case MeasurementUnit.Value(value) => s"${value}mu"
  end toString
end MeasurementUnit
