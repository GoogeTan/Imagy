package me.katze.imagy.example
package update

@FunctionalInterface
trait EventConsumer[+T, +F[+_], -DownEvent, +UpEvent]:
  def processEvent(event : DownEvent) : F[EventProcessResult[T, UpEvent]]
end EventConsumer
