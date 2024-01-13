package me.katze.imagy.components

import me.katze.imagy.common.Nat

final case class Weighted[T](value : T, weight : Nat)

object Weighted:
  final type MaybeWeighted[T] = T | Weighted[T]
end Weighted
