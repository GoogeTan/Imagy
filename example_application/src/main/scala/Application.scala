package me.katze.imagy.example

import update.{ ApplicationEvent, ApplicationRequest }

import cats.*
import cats.effect.*
import cats.effect.IO.asyncForIO

def ioApplication[Widget[-DownEvent, +UpEvent]](
                                                  tree : Widget[ApplicationEvent, ApplicationRequest],
                                                  drawLoop : DrawLoop[IO, Widget[ApplicationEvent, ApplicationRequest]],
                                                  updateLoop : UpdateLoop[IO, [T] =>> Widget[T, ApplicationRequest], ApplicationEvent],
                                                ) : IO[ExitCode] =
  applicationLoop[
    IO,
    ApplicationEvent,
    [T] =>> Widget[T, ApplicationRequest]
  ](tree, drawLoop, updateLoop).flatMap(_.join)
end ioApplication
