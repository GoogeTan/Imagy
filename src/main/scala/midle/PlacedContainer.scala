package me.katze.imagy
package midle

trait PlacedContainer[F[+_]]:
  def multipleFixedElements[E](values : List[Placed[F[E]]], rect: Rect) : Placed[F[E]]
end PlacedContainer