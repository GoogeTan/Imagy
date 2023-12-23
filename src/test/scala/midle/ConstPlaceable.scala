package me.katze.imagy
package midle

class ConstPlaceable[F[+_]](rect : Rect, pure : [E] => E => F[E], override val weight: Option[Int] = None) extends Placeable[F[Any]]:
  override def placeInside(rect: Rect): Placed[F[Any]] =
    Placed(pure(null), this.rect)
  end placeInside
end ConstPlaceable