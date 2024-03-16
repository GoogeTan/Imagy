package me.katze.imagy.example

import draw.Drawable
import update.{ EventConsumer, EventProcessResult, IOFinishedEvent }

trait Widget[F[+_], G, -DownEvent, +UpEvent] extends Drawable[G]:
  def processEvent(event: DownEvent | IOFinishedEvent): F[EventProcessResult[Widget[F, G, DownEvent, UpEvent], UpEvent]]
end Widget


object Widget:
  
  given eventConsumer[F[+_], G, DownEvent, UpEvent] : EventConsumer[Widget[F, G, DownEvent, UpEvent], F, DownEvent | IOFinishedEvent, UpEvent] with
    extension (self: Widget[F, G, DownEvent, UpEvent]) 
      override def processEvent(event: DownEvent | IOFinishedEvent): F[EventProcessResult[Widget[F, G, DownEvent, UpEvent], UpEvent]] =
        self.processEvent(event)
      end processEvent
  end eventConsumer
end Widget

