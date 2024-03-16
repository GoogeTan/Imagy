package me.katze.imagy.example
package update

import update.ApplicationRequest

import cats.effect.ExitCode

trait ProcessRequest[F[_]]:
  extension (request: ApplicationRequest)
    def process: F[Option[ExitCode]]
  end extension
end ProcessRequest
