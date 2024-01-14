package me.katze.imagy.components
package text

import cats.Monoid

trait Text[Widget[+_]]:
  type Font
  
  def text(
            value : String,
            options: FontOptions[Font] = Monoid.empty
          ) : Widget[Nothing]
  
  // TODO добавить опции модификации внешнего вида поля ввода.
  def textInput[T](
                    initialValue : String,
                    options: FontOptions[Font] = Monoid.empty,
                    onChanged : String => T
                  ) : Widget[T]
end Text
