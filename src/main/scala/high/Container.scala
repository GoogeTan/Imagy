package me.katze.imagy
package high

trait Container[Widget[+_]]:
  def column[E](
                 elements: List[Widget[E]],
                 width: PlaceType,
                 height: PlaceType
               ): Widget[E]
  
  def row[E](
              elements: List[Widget[E]],
              width: PlaceType,
              height: PlaceType
            ): Widget[E]
end Container

enum PlaceType:
  case Weight(value : Int)
  case Fixed(value : Int)
end PlaceType
