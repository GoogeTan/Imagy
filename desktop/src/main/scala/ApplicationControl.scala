package me.katze.imagy.desktop

/**
 * Позволяет управлять потоками приложения.
 * @param close закрывает приложение
 * @param pushEvent отправляет внешнее событие корневому виджету.
 * @tparam DownEvent тип событий
 */
final case class ApplicationControl[+F[_], -DownEvent](close : F[Unit], pushEvent : DownEvent => F[Unit])
