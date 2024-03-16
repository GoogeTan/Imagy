package me.katze.imagy.example
package place

import me.katze.imagy.layout.bound.Bounds

trait ApplicationBounds[+F[_]]:
  def bounds : F[Bounds]
end ApplicationBounds
