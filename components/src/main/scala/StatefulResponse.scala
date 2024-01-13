package me.katze.imagy.components

enum StatefulResponse[+F[+_], +ChildEvent, +ParentEvent]:
  case ThrowEvent(parentEvent: ParentEvent)
  case DoIO(task : F[ChildEvent])
end StatefulResponse
