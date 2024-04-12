package me.katze.imagy.example

import draw.{ Drawable, SwingProcessRequest }
import update.{ ApplicationEvent, ApplicationRequest, EventConsumer, IOFinishedEvent }

import cats.effect.{ ExitCode, IO, IOApp }

trait ImagyApp[
  PlacedWidget[A, B] <: Drawable[IO[Unit]] & EventConsumer[FreeWidget[A, ApplicationRequest], IO, A, B],
  FreeWidget[A, B] <: Placeable[IO, PlacedWidget[A, B]],
  DownEvent
] extends IOApp:
  final type DefaultWidgetP[A] = PlacedWidget[A | IOFinishedEvent, ApplicationRequest]
  final type DefaultWidgetF[A] = FreeWidget[A | IOFinishedEvent, ApplicationRequest]
  
  override def run(args: List[String]): IO[ExitCode] =
    for
      swing <- draw.initSwing
      given SwingProcessRequest = SwingProcessRequest(swing)
      widget <- widget(args)
      a <- applicationLoop[IO, DownEvent | ApplicationEvent, DefaultWidgetP, DefaultWidgetF](
        widget,
        drawLoop(drawLoopExceptionHandler, swing.graphics),
        updateLoop[IO, DefaultWidgetP, DefaultWidgetF, DownEvent | ApplicationEvent]
      )
      code <- a.join
    yield code
  end run
  
  def widget(args : List[String]) : IO[DefaultWidgetF[DownEvent | ApplicationEvent]]
  
  def drawLoopExceptionHandler(exception: Throwable): IO[Option[ExitCode]] =
    IO.println(s"Error in draw loop: $exception").map(_ => Some(ExitCode.Error))
  end drawLoopExceptionHandler
end ImagyApp
