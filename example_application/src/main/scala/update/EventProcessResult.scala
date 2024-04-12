package me.katze.imagy.example
package update

final case class EventProcessResult[+FreeWidget, +Event](freeWidget : FreeWidget, events : List[Event])