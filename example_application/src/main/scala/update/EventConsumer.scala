package me.katze.imagy.example
package update

@FunctionalInterface
trait EventConsumer[T, +F[+_], -DownEvent, +UpEvent]:
  /**
   * TODO Написать доку
   * @return
   */
  extension (self : T)
    def processEvent(event : DownEvent) : F[EventProcessResult[T, UpEvent]]
  end extension  
end EventConsumer

