package me.katze.imagy.example

import draw.{ Drawable, SwingProcessRequest }
import update.{ ApplicationEvent, ApplicationRequest, EventConsumer, IOFinishedEvent }

import cats.effect.{ ExitCode, IO, IOApp }

trait ImagyApp[
  Widget[-A, +B] <: Drawable[IO[Unit]] & EventConsumer[Widget[A, ApplicationRequest], IO, A, B],
  DownEvent
] extends IOApp:
  final type DefaultWidget[A] = Widget[A | IOFinishedEvent, ApplicationRequest]
  
  
  override def run(args: List[String]): IO[ExitCode] =
    for
      swing <- draw.initSwing
      given SwingProcessRequest = SwingProcessRequest(swing)
      widget <- widget(args)
      a <- applicationLoop[IO, DownEvent | ApplicationEvent, DefaultWidget](
        widget,
        drawLoop(drawLoopExceptionHandler, swing.graphics),
        updateLoop
      )
      code <- a.join
    yield code
  end run
  
  def widget(args : List[String]) : IO[DefaultWidget[DownEvent | ApplicationEvent]]
  
  def drawLoopExceptionHandler(exception: Throwable): IO[Option[ExitCode]] =
    IO.println(s"Error on exception thread: $exception").map(_ => Some(ExitCode.Error))
  end drawLoopExceptionHandler
end ImagyApp
