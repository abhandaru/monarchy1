package monarchy.controllers

import scala.concurrent.{Future, ExecutionContext}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import monarchy.dal

class RootController(implicit ec: ExecutionContext, queryCli: dal.QueryClient) extends Controller {
  import dal.PostgresProfile.Implicits._
  override def action(ctx: RequestContext) = {
    val q = dal.UserQuery.filter(_.id === 1L)
    queryCli.first(q).map { userMaybe =>
      HttpResponse(StatusCodes.OK, entity = userMaybe.toString)
    }
  }
}
