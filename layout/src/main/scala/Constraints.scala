package me.katze.imagy.layout

final case class Constraints(horizontal : AxisConstraints, vertical : AxisConstraints):
  /**
   * `false` when [maxWidth] is [Infinity] and `true` if [maxWidth] is a non-[Infinity] value.
   */
  val hasBoundedWidth: Boolean = horizontal.bounded
  
  /**
   * `false` when [maxHeight] is [Infinity] and `true` if [maxHeight] is a non-[Infinity] value.
   */
  val hasBoundedHeight: Boolean = vertical.bounded
  
  /**
   * Whether there is exactly one width value that satisfies the constraints.
   */
  val hasFixedWidth: Boolean = horizontal.fixed
  
  /**
   * Whether there is exactly one height value that satisfies the constraints.
   */
  val hasFixedHeight: Boolean = vertical.fixed
  
  /**
   * Whether the area of a component respecting these constraints will definitely be 0.
   * This is true when at least one of maxWidth and maxHeight are 0.
   */
  val isZero: Boolean = vertical.zero || horizontal.zero
  
  def withMaxHeight(newMaxHeight : Int) : Constraints =
    copy(
      vertical = vertical.copy(max = newMaxHeight, min = math.min(vertical.min, newMaxHeight))
    )
  end withMaxHeight
  
  def withMaxWidth(newMaxWeight : Int) : Constraints =
    copy(
      horizontal = horizontal.copy(max = newMaxWeight, min = math.min(horizontal.min, newMaxWeight))
    )
  end withMaxWidth
end Constraints

object Constraints:
  inline val Infinity: Int.MaxValue.type = Int.MaxValue
end Constraints
