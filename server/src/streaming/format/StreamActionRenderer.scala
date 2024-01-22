package monarchy.streaming.format

import monarchy.streaming.core._
import monarchy.util.{Json, Async}
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

trait ActionRenderer[T <: StreamAction] extends (T => Future[Option[String]])

abstract class BasicActionRenderer[T <: StreamAction: ClassTag] extends ActionRenderer[T] {
  implicit def ec: ExecutionContext
  def render(axn: T): Future[_]
  def name: String = implicitly[ClassTag[T]].runtimeClass.getSimpleName
  override def apply(axn: T): Future[Option[String]] = {
    render(axn).map { data =>
      Some(Json.stringify(Map("name" -> name, "data" -> data)))
    }
  }
}

class PongRenderer(implicit val ec: ExecutionContext) extends BasicActionRenderer[Pong] {
  override def render(axn: Pong) = Future.successful(axn.t.toEpochMilli)
}

class RedisRawRenderer(implicit val ec: ExecutionContext) extends BasicActionRenderer[RedisRaw] {
  override def render(axn: RedisRaw) = Future.successful(axn.text)
}

class GameCreateRenderer(implicit val ec: ExecutionContext) extends BasicActionRenderer[GameCreate] {
  override def render(axn: GameCreate) = Future.successful(Map("gameId" -> axn.gameId))
}
