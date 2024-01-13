package me.katze.imagy
package components

import common.{ Matrix, Nat }
import me.katze.imagy.components.Weighted.MaybeWeighted

trait Container[Widget[+_]]:
  def column[E](elements: List[MaybeWeighted[Widget[E]]]): Widget[E]
  
  def row[E](elements: List[MaybeWeighted[Widget[E]]]): Widget[E]
  
  def grid[E, n <: Int, m <: Int](elements : Matrix[Widget[E], n, m]) : Widget[E]
end Container
