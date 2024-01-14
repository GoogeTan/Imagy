package me.katze.imagy.layout

import Constraints.Infinity

/**
 *
 * @param minWidth The minimum width that the measurement can take, in pixels.
 * @param maxWidth The maximum width that the measurement can take, in pixels. This will either be a positive value greater than or equal to [minWidth] or [Constraints.Infinity].
 * @param minHeight The minimum height that the measurement can take, in pixels.
 * @param maxHeight The maximum height that the measurement can take, in pixels. This will either be a positive value greater than or equal to [minHeight] or [Constraints.Infinity].
 */
final case class Constraints(minWidth: Int, maxWidth: Int, minHeight: Int, maxHeight: Int):
  /**
   * `false` when [maxWidth] is [Infinity] and `true` if [maxWidth] is a non-[Infinity] value.
   */
  val hasBoundedWidth: Boolean = maxWidth != Infinity
  
  /**
   * `false` when [maxHeight] is [Infinity] and `true` if [maxHeight] is a non-[Infinity] value.
   */
  val hasBoundedHeight: Boolean = maxHeight != Infinity
  
  /**
   * Whether there is exactly one width value that satisfies the constraints.
   */
  val hasFixedWidth: Boolean = maxWidth == minWidth
  
  /**
   * Whether there is exactly one height value that satisfies the constraints.
   */
  val hasFixedHeight: Boolean = maxHeight == minHeight
  
  /**
   * Whether the area of a component respecting these constraints will definitely be 0.
   * This is true when at least one of maxWidth and maxHeight are 0.
   */
  val isZero: Boolean = maxWidth == 0 || maxHeight == 0
  
  def withMaxHeight(newMaxHeight : Int) : Constraints =
    copy(
      maxHeight = newMaxHeight,
      minHeight = math.min(minHeight, newMaxHeight)
    )
  end withMaxHeight
end Constraints

object Constraints:
  inline val Infinity: Int.MaxValue.type = Int.MaxValue
end Constraints
