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
