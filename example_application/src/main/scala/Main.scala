package me.katze.imagy.example

import draw.{ SimpleDrawApi, SwingProcessRequest }
import update.{ ApplicationEvent, ApplicationRequest, EventProcessResult, IOFinishedEvent }

import cats.effect.{ ExitCode, IO, IOApp }
import cats.syntax.all.{ *, given }
import Widget.{ *, given }

import cats.Applicative

import java.awt.Color
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

object Main extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    for
      swing <- draw.initSwing
      given SwingProcessRequest = SwingProcessRequest(swing)
      a <- applicationLoop[IO, ApplicationEvent, [A] =>> Widget[IO, IO[Unit], A, ApplicationRequest]] (
        TestWidget(swing.graphics),
        drawLoop[IO, Throwable](drawLoopExceptionHandler, swing.graphics),
        updateLoop[IO, [A] =>> Widget[IO, IO[Unit], A, ApplicationRequest], ApplicationEvent](using Widget.eventConsumer[IO, IO[Unit], ApplicationEvent, ApplicationRequest])
      )
      _ <- everySecond(a.pushEvent).start
      code <- a.join
    yield code
  end run
  
  private def drawLoopExceptionHandler(exception : Throwable) : IO[Option[ExitCode]] =
    IO.println(s"Error on exception thread: $exception").map(_ => Some(ExitCode.Error))
  end drawLoopExceptionHandler
  
  private def everySecond(pushEvent : ApplicationEvent => IO[Unit]) : IO[Unit] =
    (
      pushEvent(ApplicationEvent.Io) *> IO.sleep(FiniteDuration(16, TimeUnit.MILLISECONDS))
    ).iterateWhile(_ => true)
  
  
  private class TestWidget[F[+_] : Applicative](api :SimpleDrawApi[F], val color : Int = 0) extends Widget[F, F[Unit], ApplicationEvent, ApplicationRequest]:
    override def draw: F[Unit] =
      api.rectangle(0, 0, 100, 100, Color.getHSBColor(color.toFloat / 256.0f, 1f, 1f).getRGB)
    end draw
    
    override def processEvent(event: ApplicationEvent | IOFinishedEvent): F[EventProcessResult[Widget[F, F[Unit], ApplicationEvent, ApplicationRequest], ApplicationRequest]] =
      Applicative[F].pure(
        event match
          case _ : ApplicationEvent =>
            EventProcessResult(TestWidget(api, color = (color + 1) % 256), Nil)
          case IOFinishedEvent(_) =>
            EventProcessResult(this, Nil)
      )
    end processEvent
  end TestWidget
end Main
