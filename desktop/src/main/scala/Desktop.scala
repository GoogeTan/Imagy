package me.katze.imagy.desktop

trait Desktop[F[_], Widget[+UpEvent, -DownEvent], -ApplicationEvent, +IOFinished]:
  /**
   * Позволяет управлять потоками приложения.
   * @param close закрывает приложение
   * @param pushEvent отправляет внешнее событие корневому виджету.
   * @tparam DownEvent тип событий
   */
  final case class ApplicationControl[-DownEvent](close : F[Unit], pushEvent : DownEvent => F[Unit])
  
  def WindowApplication[DownEvent](root : Widget[ApplicationEvent, DownEvent | IOFinished], fullScreen : Boolean) : F[ApplicationControl[DownEvent]]
end Desktop
