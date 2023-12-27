package me.katze.imagy
package midle

import common.Nat

import io.github.iltotore.iron.constraint.numeric.Greater
import io.github.iltotore.iron.{ *, given }
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import cats.*

import scala.util.Random
/*
import cats.data.*
import cats.effect.std.*
import cats.effect.syntax.all.{ *, given }*/
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.freespec.AsyncFreeSpec

class ColumnPlaceableTest extends AnyFlatSpec with should.Matchers:
  private val defaultPlaceableSize = Rect(20, 31)
  private val defaultRestrictions = Rect(100, 100)
  case class Widget[+T]()
  
  def testColumnPlaceable[E](
                              elements: List[Placeable[Widget[E]]],
                              weight: Option[Int :| Greater[0]],
                            ): ColumnPlaceable[Widget[E]] =
    ColumnPlaceable(
      elements,
      weight,
      (placeds, _) => Placed(Widget(), Rect(placeds.map(_.width).maxOption.getOrElse(0).refine, placeds.map(_.height).sum.refine))
    )
  private def constPlaceable(rect : Rect = defaultPlaceableSize, weight : Option[Int :| Greater[0]] = None) =
    ConstPlaceable(rect, [E] => (_ : E) => Widget(), weight)
  
  "Empty ColumnPlaceable" should "have zero size" in :
    val c = testColumnPlaceable(Nil, None)
    val result = c.placeInside(defaultRestrictions)
    result.rect should be(Rect(0, 0))
  
  "One element ColumnPlaceable" should "have the same size as element" in :
    val c = testColumnPlaceable(List(constPlaceable()), None)
    val result = c.placeInside(defaultRestrictions)
    result.rect should be(defaultPlaceableSize)
    
  "Two elements ColumnPlaceable" should "have the sum size" in :
    val c = testColumnPlaceable(List(constPlaceable(), constPlaceable()), None)
    val result = c.placeInside(defaultRestrictions)
    result.rect should be(defaultPlaceableSize.copy(height = (defaultPlaceableSize.height * 2).refine))
  
  "weightOf(Nil)" should "have zero weight" in :
    ColumnPlaceable.weightOf(Nil) should be(0)
  
  "fixedHeight" should "be the same as not weighted element size" in :
    ColumnPlaceable.fixedHeight(constPlaceable(), defaultRestrictions) should be(defaultPlaceableSize.height)
    
  private val testSizes = (for
    tryIndex <- 0 to 1000
    random = Random(tryIndex)
    count = random.nextInt(10_000)
  yield count).toSet.toList.sorted
  
  for
    count <- testSizes
    random = Random(count ^ 2141)
  do
    val constElements = (0 until count).map(_ => constPlaceable()).toList
    val weights = (0 until count).map(_ => (random.nextInt(100) + 1).refine[Greater[0]]).toList
    val weightedElements = weights.map(weight => constPlaceable(weight = Some(weight)))
    val mixedElements = random.shuffle(constElements ++ weightedElements)
    
    s"$count elements ColumnPlaceable" should s"have the ${defaultPlaceableSize.height * count} size" in:
      val c = testColumnPlaceable(constElements, None)
      val result = c.placeInside(defaultRestrictions)
      result.rect should be(defaultPlaceableSize.copy(height = (defaultPlaceableSize.height * count).refine, width = if count == 0 then 0 else defaultPlaceableSize.width))
    
    s"weightOf($count)" should s"have sum weight" in:
      ColumnPlaceable.weightOf(weightedElements) should be(weights.sum)
    
    s"weightOf($count)" should s"ignore static elements" in:
      ColumnPlaceable.weightOf(mixedElements) should be(weights.sum)
    
    s"fixedHeight($count)" should "have sum height" in:
      ColumnPlaceable.fixedHeight(
        mixedElements,
        defaultRestrictions
      ) should be(constElements.map(_.rect.height).sum)
end ColumnPlaceableTest
