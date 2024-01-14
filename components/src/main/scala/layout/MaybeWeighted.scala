package me.katze.imagy.components
package layout

import me.katze.imagy.common.Nat

enum MaybeWeighted[+T]:
  case Weighted(value: T, weight: Nat)
  case Const(value: T)
end MaybeWeighted
