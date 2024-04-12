package rowcolumn

import me.katze.imagy.layout.{ Axis, Placed, Sized }
import me.katze.imagy.layout.rowcolumn.*
import org.scalatest.*
import org.scalatest.flatspec.*
import org.scalatest.matchers.*
import io.github.iltotore.iron.{ *, given }
import me.katze.imagy.components.layout.strategy.{ Begin, Center, End, SpaceBetween }
import me.katze.imagy.layout.Axis.{ Horizontal, Vertical }
import me.katze.imagy.layout.bound.{ AxisBounds, AxisDependentBounds, Bounds }
import me.katze.imagy.layout.unit.MeasurementUnit
import me.katze.imagy.layout.unit.MeasurementUnit.{ Infinite, Value }
import org.scalatest.enablers.Length.{*, given}
import scala.List
import me.katze.imagy.layout.bound.constraints.{*, given}

class RowColumnPlacementTest extends AnyFlatSpec with should.Matchers:
  case object TestWidget
  
  val tenSized: Sized[TestWidget.type] = Sized(TestWidget, 10, 20)
  val infiniteAxisBounds      : AxisBounds          = AxisBounds(Value(0), Infinite)
  val infiniteBounds: Bounds = Bounds(infiniteAxisBounds, infiniteAxisBounds)
  
  val infiniteBoundsHorizontal: AxisDependentBounds = AxisDependentBounds.fromConstraints(infiniteBounds, Horizontal)
  val infiniteBoundsVertical  : AxisDependentBounds = AxisDependentBounds.fromConstraints(infiniteBounds, Vertical)
  
  val finiteOverbounds: Bounds = Bounds(
    AxisBounds(MeasurementUnit.Value(0), MeasurementUnit.Value(102)),
      AxisBounds(MeasurementUnit.Value(0), MeasurementUnit.Value(201))
  )
  val finiteBounds: Bounds = Bounds(
    AxisBounds(MeasurementUnit.Value(0), MeasurementUnit.Value(30)),
    AxisBounds(MeasurementUnit.Value(0), MeasurementUnit.Value(60))
  )
  
  
  val finiteOverboundsVertical: AxisDependentBounds = AxisDependentBounds.fromConstraints(finiteOverbounds, Axis.Vertical)
  val finiteOverboundsHorizontal: AxisDependentBounds = AxisDependentBounds.fromConstraints(finiteOverbounds, Axis.Horizontal)
  val finiteBoundsHorizontal: AxisDependentBounds = AxisDependentBounds.fromConstraints(finiteBounds, Axis.Horizontal)
  val finiteBoundsVertical: AxisDependentBounds = AxisDependentBounds.fromConstraints(finiteBounds, Axis.Vertical)
  
  "Row((10, 20), (10, 20), (10, 20))" should s"be (0, 0) (${tenSized.width}, 0) (${tenSized.width * 2}, 0) in $infiniteBoundsHorizontal" in:
    rowColumnPlace(List(tenSized, tenSized, tenSized), Begin, Begin, infiniteBoundsHorizontal.refine) should be(List(
      Placed(TestWidget, 0, 0, tenSized.width, tenSized.height),
      Placed(TestWidget, tenSized.width, 0, tenSized.width, tenSized.height),
      Placed(TestWidget, (tenSized.width * 2).refine, 0, tenSized.width, tenSized.height),
    ))
    
  "Row((10, 20), (10, 20), (10, 20))" should s"be (0, 0) (${tenSized.width}, 0) (${tenSized.width * 2}, 0) in $infiniteBoundsVertical" in :
    rowColumnPlace(List(tenSized, tenSized, tenSized), Begin, Begin, infiniteBoundsHorizontal.refine) should be(List(
      Placed(TestWidget, 0, 0, tenSized.width, tenSized.height),
      Placed(TestWidget, tenSized.width, 0, tenSized.width, tenSized.height),
      Placed(TestWidget, (tenSized.width * 2).refine, 0, tenSized.width, tenSized.height),
    ))
    
  "Column((10, 20), (10, 20), (10, 20))" should s"be (0, 0) (0, ${tenSized.height} + 47) (0, ${tenSized.height * 2} + ${47*2}) in $finiteOverboundsVertical using ${SpaceBetween}" in :
    rowColumnPlace(List(tenSized, tenSized, tenSized), SpaceBetween, Begin, finiteOverboundsVertical.refine) should be(List(
      Placed(TestWidget, 0, 0, tenSized.width, tenSized.height),
      Placed(TestWidget, 0, (tenSized.height + 47).refine, tenSized.width, tenSized.height),
      Placed(TestWidget, 0, (tenSized.height * 2 + 47*2).refine, tenSized.width, tenSized.height),
    ))
  
  "Row((10, 20), (10, 20), (10, 20))" should s"be (0, 0) (${tenSized.width} + 24) (${tenSized.height * 2} + ${24*2}, 0) in $finiteOverboundsHorizontal using ${SpaceBetween}" in :
    rowColumnPlace(List(tenSized, tenSized, tenSized), SpaceBetween, Begin, finiteOverboundsHorizontal.refine) should be(List(
      Placed(TestWidget, 0, 0, tenSized.width, tenSized.height),
      Placed(TestWidget, (tenSized.width + 24).refine, 0, tenSized.width, tenSized.height),
      Placed(TestWidget, (tenSized.width * 2 + 24 * 2).refine, 0, tenSized.width, tenSized.height),
    ))
    
  "RowColumn in matching bounds" should "not depend on main axis strategy" in:
    val elements = List(tenSized, tenSized, tenSized)
    val strategies = Set(Begin, Center, End, SpaceBetween)
    
    strategies.map(strategy => 
      given strategy.type = strategy
      rowColumnPlace(elements, strategy, Begin, finiteBoundsHorizontal.refine)
    ) should have size (1)
    strategies.map(strategy =>
      given strategy.type = strategy
      rowColumnPlace(elements, strategy, Begin, finiteBoundsVertical.refine)
    ) should have size (1)
  
  "RowColumn in infinite bounds with End strategy" should "throw IllegalStateException exception" in :
    val elements = List(tenSized, tenSized, tenSized)
    assertThrows[IllegalArgumentException](rowColumnPlace(elements, End, Begin, infiniteBoundsHorizontal.refine))
end RowColumnPlacementTest
