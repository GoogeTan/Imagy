package me.katze.imagy.components
package layout

import cats.Functor
import me.katze.imagy.common.Nat

final case class MaybeWeighted[+T](value : T, weight : Option[Nat])


given Functor[MaybeWeighted] with
  override def map[A, B](fa: MaybeWeighted[A])(f: A => B): MaybeWeighted[B] =
    MaybeWeighted(f(fa.value), fa.weight)
  end map
end given
