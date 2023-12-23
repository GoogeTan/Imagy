package me.katze.imagy
package midle

import high.FontOptions

trait FontSize:
  def charSize(char: Char, options : FontOptions) : (Int, Int)
end FontSize
