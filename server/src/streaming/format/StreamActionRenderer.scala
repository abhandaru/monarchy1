package monarchy.streaming.format

import monarchy.streaming.core._
import monarchy.util.{Json, Async}
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

case class StreamActionRenderer(receive: PartialFunction[StreamAction, Future[String]])(
  implicit ec: ExecutionContext) {

  def apply(action: StreamAction): Future[Option[String]] = {
    receive.isDefinedAt(action) match {
      case true => receive(action).map(Some(_))
      case false => Async.None
    }
  }
}

abstract class ActionRenderer[T <: StreamAction: ClassTag] extends (T => Future[String]) {
  implicit def ec: ExecutionContext
  def render(axn: T): Future[_]
  def name: String = implicitly[ClassTag[T]].runtimeClass.getSimpleName
  override def apply(axn: T): Future[String] = {
    render(axn).map { data =>
      Json.stringify(Map("name" -> name, "data" -> data))
    }
  }
}

class PongRenderer(implicit val ec: ExecutionContext) extends ActionRenderer[Pong] {
  override def render(axn: Pong) = Future.successful(axn.at)
}

class RedisRawRenderer(implicit val ec: ExecutionContext) extends ActionRenderer[RedisRaw] {
  override def render(axn: RedisRaw) = Future.successful(axn.text)
}

class GameCreateRenderer(implicit val ec: ExecutionContext) extends ActionRenderer[GameCreate] {
  override def render(axn: GameCreate) = Future.successful(Map("gameId" -> axn.gameId))
}
