package me.katze.imagy.components
package layout.strategy

trait AdditionalAxisStrategy
trait MainAxisStrategy

case object Begin extends AdditionalAxisStrategy with MainAxisStrategy
case object Center extends AdditionalAxisStrategy with MainAxisStrategy
case object End extends AdditionalAxisStrategy with MainAxisStrategy
case object SpaceBetween extends MainAxisStrategy
