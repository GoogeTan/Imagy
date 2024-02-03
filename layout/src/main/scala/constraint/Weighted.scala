package me.katze.imagy.layout
package constraint

import io.github.iltotore.iron.Constraint
import me.katze.imagy.components.layout.MaybeWeighted

final class Weighted

given nnn[T]: Constraint[MaybeWeighted[T], Weighted] with
  override inline def test(value: MaybeWeighted[T]): Boolean = value.weight.isDefined
  
  override inline def message: String = "MaybeWeighted must have some weight"
end nnn

