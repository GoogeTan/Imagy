package me.katze.imagy.example
package update

import cats.effect.ExitCode

enum ApplicationRequest:
  case BecomeFullScreen
  case BecomeNotFullScreen
  case CloseApp(code : ExitCode)
end ApplicationRequest
