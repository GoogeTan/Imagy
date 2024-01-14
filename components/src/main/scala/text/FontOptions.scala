package me.katze.imagy.components
package text

import cats.Monoid

final case class FontOptions[Font](weight: Option[Int] = None, size: Option[Int] = None, font: Option[Font] = None)

object FontOptions:
  given[Font]: Monoid[FontOptions[Font]] =
    Monoid.instance(
      FontOptions(None, None, None),
      (a : FontOptions[Font], b : FontOptions[Font]) =>
        FontOptions(
          b.weight.orElse(a.weight), 
          b.size.orElse(a.size), 
          b.font.orElse(a.font)
        )
    )
  end given
end FontOptions
