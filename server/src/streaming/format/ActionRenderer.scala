package monarchy.streaming.format

import monarchy.streaming.core._
import monarchy.util.{Json, Async}
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

trait ActionRenderer[A <: StreamAction] extends (A => Future[Option[String]])

object ActionRenderer {
  abstract class Impl[A <: StreamAction](implicit
      ec: ExecutionContext,
      tag: ClassTag[A]
  ) extends ActionRenderer[A] {
    // Determine when and how to output some encoded data.
    def renderOpt(axn: A): Future[Option[_]]

    def name: String =
      tag.runtimeClass.getSimpleName
    
    override def apply(axn: A): Future[Option[String]] = {
      renderOpt(axn).map { 
        case None => None
        case Some(data) =>
          Some(Json.stringify(Map("name" -> name, "data" -> data)))
      }
    }
  }

  abstract class Const[A <: StreamAction: ClassTag](ext: A => Any)(
      implicit ec: ExecutionContext
  ) extends Impl[A] {
    override def renderOpt(axn: A) = Future.successful(Some(ext(axn)))
  }
}

class PongRenderer(implicit ec: ExecutionContext)
    extends ActionRenderer.Const[Pong](_.t.toEpochMilli)

class RedisRawRenderer(implicit ec: ExecutionContext)
    extends ActionRenderer.Const[RedisRaw](_.text)

class GameCreateRenderer(implicit ec: ExecutionContext)
    extends ActionRenderer.Const[GameCreate](axn => Map("gameId" -> axn.gameId))

class GameChangeRenderer(implicit ec: ExecutionContext) extends ActionRenderer.Impl[GameChange] {
  override def renderOpt(axn: GameChange) = {
    val data = Option(axn)
      .filterNot(_.action == "SELECT")
      .map { a => Map("gameId" -> axn.gameId, "action" -> axn.action) }
     Future.successful(data)
  }
}
