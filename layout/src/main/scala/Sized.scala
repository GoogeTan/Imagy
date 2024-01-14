package me.katze.imagy.layout

import me.katze.imagy.common.ZNat

final case class Sized[+T](value : T, width : ZNat, height : ZNat)