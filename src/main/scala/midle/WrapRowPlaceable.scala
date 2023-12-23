package me.katze.imagy
package midle

class WrapRowPlaceable[T, B](
                                val placeables : List[Placeable[T]],
                                val fold : (T, T) => B,
                                override val weight: Option[Int]
                             ) extends Placeable[B]:
  override def placeInside(rect: Rect): Placed[B] =
    ???