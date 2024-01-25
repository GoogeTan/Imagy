package me.katze.imagy.components
package layout

import me.katze.imagy.common.Nat

final case class MaybeWeighted[+T](value : T, weight : Option[Nat])
