package me.katze.imagy.example

trait IOMaster[F[_]]:
  def pushIO(io: F[Any], path: Path, keepAliveAfterWidgetDetach: Boolean): F[Unit]
  
  def alive: F[Set[Path]]
  
  def detach(path: Path): F[Unit]
end IOMaster
