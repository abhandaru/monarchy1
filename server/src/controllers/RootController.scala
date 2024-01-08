package monarchy.controllers

import scala.concurrent.{Future, ExecutionContext}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import monarchy.dal

class RootController(implicit ec: ExecutionContext, queryCli: dal.QueryClient) extends GetController {
  import dal.PostgresProfile.Implicits._

  override def action(ctx: AuthContext) = {
    val q = dal.User.query.filter(_.id === 1L)
    queryCli.first(q).map { userMaybe =>
      val userText = userMaybe.map { u => s"User(id=${u.id}, username=${u.username})" }
      HttpResponse(StatusCodes.OK, entity = userText.toString)
    }
  }
}
