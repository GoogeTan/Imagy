package me.katze.imagy.example
package draw

trait Drawable[+F]:
  def draw : F
end Drawable

