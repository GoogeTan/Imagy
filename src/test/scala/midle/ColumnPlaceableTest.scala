package me.katze.imagy
package midle


import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should


class ColumnPlaceableTest extends AnyFlatSpec with should.Matchers:
  private val defaultPlaceableSize = Rect(1, 5, 20, 31)
  private val defaultRectictions = Rect(0, 0, 100, 100)
  case class Widget[+T]()

  given PlacedContainer[Widget] = PlacedContainerImpl([E] => () => Widget(), [E] => (a : Widget[E], b : Widget[E]) => Widget())
  
  private def constPlaceable(rect : Rect = defaultPlaceableSize, weight : Option[Int] = None) =
    ConstPlaceable(rect, [E] => (* : E) => Widget(), weight)
  
  
  "Empty ColumnPlaceable" should "have zero size" in:
    val c = ColumnPlaceable[Widget, Nothing](Nil, None)
    val result = c.placeInside(defaultRectictions)
    result.rect should be(Rect(0, 0, 0, 0))
  
  "One element ColumnPlaceable" should "have the same size as element" in:
    val c = ColumnPlaceable(List(constPlaceable()), None)
    val result = c.placeInside(defaultRectictions)
    result.rect should be(defaultPlaceableSize)
  
  "Two elements ColumnPlaceable" should "have the sum size" in :
    val c = ColumnPlaceable(List(constPlaceable(), constPlaceable()), None)
    val result = c.placeInside(defaultRectictions)
    result.rect should be(defaultPlaceableSize.copy(height = defaultPlaceableSize.height * 2))
  
  
  "weightOf(Nil)" should "have zero weight" in:
    ColumnPlaceable.weightOf(Nil) should be(0)
  
  "weightOf" should "have sum weight" in:
    ColumnPlaceable.weightOf(
      List(
        constPlaceable(weight = Some(10)),
        constPlaceable(weight = Some(21))
      )
    ) should be(31)
  
  "weightOf" should "ignore static elements" in:
    ColumnPlaceable.weightOf(
      List(
        constPlaceable(weight = Some(10)),
        constPlaceable(),
        constPlaceable(weight = Some(7))
      )
    ) should be(17)
    
  "fixedHeight" should "ignore weighted elements" in:
    ColumnPlaceable.fixedHeight(constPlaceable(weight = Some(1)), defaultRectictions) should be(0)
  
  
  "fixedHeight" should "be the same as not weighted element size" in:
    ColumnPlaceable.fixedHeight(constPlaceable(), defaultRectictions) should be(defaultPlaceableSize.height)
    
  "fixedHeight(list)" should "have sum height" in:
    ColumnPlaceable.fixedHeight(
      List(
        constPlaceable(weight = Some(10)),
        constPlaceable(),
        constPlaceable(weight = Some(7)),
        constPlaceable(defaultPlaceableSize.copy(height = 17)),
      ),
      defaultRectictions
    ) should be(17 + defaultPlaceableSize.height)
    
    
end ColumnPlaceableTest
