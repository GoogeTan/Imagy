package me.katze.imagy.example
package update

final case class EventProcessResult[+T, +E](value : T, events : List[E])