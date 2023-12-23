package me.katze.imagy
package common

import scala.annotation.targetName

object typelevel:
  @targetName("type_composition")
  infix type +>[F[_], G[_]] = [A] =>> F[G[A]]
end typelevel
