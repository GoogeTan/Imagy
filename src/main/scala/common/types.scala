package me.katze.imagy
package common

import io.github.iltotore.iron.:|
import io.github.iltotore.iron.constraint.numeric.Greater

/**
 * Натуральное число
 */
type Nat = Int :| Greater[0]
/**
 * Не отрицательное число
 */
type ZNat = Int :| Greater[-1]