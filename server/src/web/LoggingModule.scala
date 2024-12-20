package monarchy.web

import com.typesafe.scalalogging.StrictLogging
import akka.http.scaladsl.server.{Directives, RequestContext, Route}
import monarchy.util.Timer
import scala.concurrent.ExecutionContext

object LoggingModule extends StrictLogging {
  import Directives._

  def log(r: Route)(implicit ec: ExecutionContext): Route = extractClientIP { ip =>
    (ctx: RequestContext) =>
      val method = ctx.request.method.name
      val uri = ctx.request.uri
      Timer.clock(r(ctx)) {
        case (st, end) =>
          val timing = end - st
          val addr = ip.toOption.map(_.getHostAddress).getOrElse("0.0.0.0")
          logger.info(s"$method $uri from <$addr> took ${timing}ms")
      }
  }
}
