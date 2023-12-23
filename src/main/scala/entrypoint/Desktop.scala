package me.katze.imagy
package entrypoint

trait Desktop[F[_], Widget[+UpEvent, -DownEvent], -ApplicationEvent, +IOFinished]:
  /**
   * Позволяет управлять потоками приложения.
   * @param close закрывает приложение
   * @param pushEvent отправляет внешнее событие корневому виджету.
   * @tparam DownEvent тип событий
   */
  case class ApplicationControl[-DownEvent](
                                              close : F[Unit],
                                              pushEvent : DownEvent => F[Unit]
                                            )
  
  case class WindowContext(screenWidth : Int, screenHeight : Int)
  
  def WindowApplication[DownEvent](root : Widget[ApplicationEvent, DownEvent | IOFinished], fullScreen : Boolean) : F[ApplicationControl[DownEvent]]
end Desktop
