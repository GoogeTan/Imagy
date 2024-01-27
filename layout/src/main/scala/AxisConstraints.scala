package me.katze.imagy.layout

final case class AxisConstraints(min: Int, max: Int):
  def fixed: Boolean = min == max
  def bounded : Boolean = max != AxisConstraints.Infinity
  
  def zero : Boolean = max == 0
  
  def withMaxValue(value : Int) : AxisConstraints =
    AxisConstraints(math.min(min, value), value)
  end withMaxValue
end AxisConstraints

object AxisConstraints:
  inline val Infinity: Int.MaxValue.type = Int.MaxValue
end AxisConstraints