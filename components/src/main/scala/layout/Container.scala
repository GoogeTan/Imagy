package me.katze.imagy.components
package layout

import layout.strategy.*

import me.katze.imagy.common.Matrix

trait Container[Widget[+_]]:
  def column[E](
                  elements: List[MaybeWeighted[Widget[E]]],
                  verticalStrategy : MainAxisStrategy = Begin,
                  horizontalStrategy : AdditionalAxisStrategy = Center,
                ): Widget[E]
  
  def row[E](
              elements: List[MaybeWeighted[Widget[E]]],
              verticalStrategy : AdditionalAxisStrategy = Center,
              horizontalStrategy : MainAxisStrategy = Begin,
            ): Widget[E]
  
  def grid[E, n <: Int, m <: Int](elements : Matrix[Widget[E], n, m]) : Widget[E]
end Container
