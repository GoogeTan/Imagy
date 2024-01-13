package me.katze.imagy
package components

import cats.Monoid

trait Text[Widget[+_]]:
  def text(
            value : String,
            options: FontOptions = Monoid.empty
          ) : Widget[Nothing]
  
  // TODO добавить опции модификации внешнего вида поля ввода.
  def textInput[T](
                    initialValue : String,
                    options: FontOptions = Monoid.empty,
                    onChanged : String => T
                  ) : Widget[T]
end Text

final case class FontOptions(wight: Option[Int] = None, size: Option[Int] = None, font: Option[String] = None)

given Monoid[FontOptions] =
  Monoid.instance(
    FontOptions(None, None, None),
    (a : FontOptions, b : FontOptions) => FontOptions(b.wight.orElse(a.wight), b.size.orElse(a.size), b.font.orElse(a.font))
  )
end given
