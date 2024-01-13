package me.katze.imagy.components

import cats.Monoid

final case class FontOptions(weight: Option[Int] = None, size: Option[Int] = None, font: Option[String] = None)

object FontOptions:
  given Monoid[FontOptions] =
    Monoid.instance(
      FontOptions(None, None, None),
      (a : FontOptions, b : FontOptions) => FontOptions(b.weight.orElse(a.weight), b.size.orElse(a.size), b.font.orElse(a.font))
    )
  end given
end FontOptions
