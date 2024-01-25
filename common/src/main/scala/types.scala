package me.katze.imagy
package common

import io.github.iltotore.iron.:|
import io.github.iltotore.iron.constraint.all.GreaterEqual
import io.github.iltotore.iron.constraint.collection.{ FixedLength, Length }
import io.github.iltotore.iron.constraint.numeric.Greater

/**
 * Натуральное число
 */
type Nat = Int :| Greater[0]

/**
 * Не отрицательное число
 */
type ZNat = Int :| GreaterEqual[0]

type Matrix[T, N <: Int, M <: Int] = List[List[T] :| FixedLength[M]] :| FixedLength[N]