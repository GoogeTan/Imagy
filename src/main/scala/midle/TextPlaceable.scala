package me.katze.imagy
package midle
import high.{ FontOptions, PlaceType }

class TextPlaceable[T](
                        value : String,
                        options: FontOptions,
                        place : Rect => List[String] => T,
                        override val weight: Option[Int]
                      )(using FontSize) extends Placeable[T]:
  override def placeInside(rect: Rect): Placed[T] =
    Placed(place(rect)(wrap(rect.width, value, options)), rect)
  end placeInside
end TextPlaceable

def wrap(maxWidth : Int, text : String, options: FontOptions)(using f : FontSize) : List[String] =
  
  def helper(words : List[String], current : String) : List[String] =
    words match
      case head :: next =>
        if width(current ++ " " ++ head, options) > maxWidth then
          current :: helper(next, head)
        else
          helper(next, current ++ " " ++ head)
      case Nil => List(current)
  helper(text.split(" ").toList, "")
end wrap

def width(word : String, options: FontOptions)(using f : FontSize) : Int =
  word.map(f.charSize(_, options)).map(_._1).sum
end width
