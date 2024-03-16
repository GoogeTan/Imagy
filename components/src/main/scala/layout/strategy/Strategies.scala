package me.katze.imagy.components
package layout.strategy

sealed trait AdditionalAxisStrategy extends MainAxisStrategy
sealed trait MainAxisStrategy

case object Begin extends AdditionalAxisStrategy
case object Center extends AdditionalAxisStrategy
case object End extends AdditionalAxisStrategy
case object SpaceBetween extends MainAxisStrategy
