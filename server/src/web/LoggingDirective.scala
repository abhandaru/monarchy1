package monarchy.web

import akka.http.scaladsl.server.{Directives, RequestContext, Route}
import monarchy.util.Timer
import scala.concurrent.ExecutionContext

class LoggingDirective(implicit ec: ExecutionContext) {
  import Directives._

  def apply(r: Route): Route = extractClientIP { ip =>
    (ctx: RequestContext) =>
      val method = ctx.request.method.name
      val uri = ctx.request.uri
      Timer.clock(r(ctx)) {
        case (st, end) =>
          val timing = end - st
          val addr = ip.toOption.map(_.getHostAddress).getOrElse("0.0.0.0")
          println(s"$method $uri from <$addr> took ${timing}ms")
      }
  }
}
