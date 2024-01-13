package me.katze.imagy.desktop

trait Desktop[+F[+_], Widget[+UpEvent, -DownEvent], -ApplicationEvent, +IOFinished]:
  def WindowApplication[DownEvent](root : Widget[ApplicationEvent, DownEvent | IOFinished], fullScreen : Boolean) : F[ApplicationControl[F, DownEvent]]
end Desktop
