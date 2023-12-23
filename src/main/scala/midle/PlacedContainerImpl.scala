package me.katze.imagy
package midle

class PlacedContainerImpl[F[+_]](
                                  empty : [E] => () => F[E],
                                  reduce : [E] => (F[E], F[E]) => F[E]
                                ) extends PlacedContainer[F]:
  override def multipleFixedElements[E](values: List[Placed[F[E]]], rect: Rect): Placed[F[E]] =
    if values == Nil then
      return Placed(empty(), Rect(0, 0, 0, 0))
    end if
    
    val rects = values.map(_.rect)
    val minX = rects.map(_.x).min
    val minY = rects.map(_.y).min
    
    Placed(
      values.map(_.value).fold(empty[E]())(reduce[E]),
      Rect(
        rects.map(_.x).min,
        rects.map(_.y).max,
        rects.map(i => i.x + i.width - minX).max,
        rects.map(i => i.y + i.height - minY).max,
      )
    )
end PlacedContainerImpl
