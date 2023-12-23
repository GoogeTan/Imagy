package me.katze.imagy
package midle

import common.typelevel.+>
import high.{ Container, PlaceType }

given ContainerImpl[F[+_] : PlacedContainer] : Container[Placeable +> F] with
  override def column[E](elements: List[Placeable[F[E]]], width: PlaceType, height: PlaceType): Placeable[F[E]] =
    ???
  end column
  
  override def row[E](elements: List[Placeable[F[E]]], width: PlaceType, height: PlaceType): Placeable[F[E]] =
    ???
  end row
end ContainerImpl
