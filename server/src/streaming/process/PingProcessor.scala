package monarchy.streaming.process

import monarchy.dal
import monarchy.streaming.core._
import monarchy.util.Async
import redis.RedisClient
import scala.concurrent.{Future, ExecutionContext}
import java.time.Instant
import scala.concurrent.duration._

class PingProcessor(implicit redisCli: RedisClient, ec: ExecutionContext)
  extends ClientActionProcessor[Ping] {
  override def apply(axn: Ping): Future[StreamAction] = {
    val userId = axn.auth.userId
    val t = Instant.now
    val tKey = t.toEpochMilli.toDouble
    redisCli.zadd(StreamingKey.Online, (tKey, userId.toString)).map(_ => Pong(t))
  }
}
