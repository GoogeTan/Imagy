package me.katze.imagy
package components

import cats.data.State

enum StatefulResponse[+F[+_], +ChildEvent, +ParentEvent]:
  case ThrowEvent(parentEvent: ParentEvent)
  case DoIO(task : F[ChildEvent])
end StatefulResponse

trait Stateful[F[+_], Widget[_]]:
  def stateful[
    S,
    ChildEvent,
    ParentEvent
  ](
      name: String,
      initialState: S,
      render: S => Widget[ChildEvent],
      catchEvent: ChildEvent => State[S, StatefulResponse[F, ChildEvent, ParentEvent]]
    ): Widget[ParentEvent]
end Stateful
