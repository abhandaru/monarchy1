package monarchy.web

import akka.actor.{ActorSystem, Props, ActorRef}
import java.net.{InetSocketAddress, URI}
import redis.RedisClient
import scala.concurrent.ExecutionContext
import monarchy.streaming.RedisActorPublisher

object RedisModule {
  val DefaultRedisUrl = "http://localhost:6379"
  val RedisUrl = sys.env.getOrElse("REDIS_URL", DefaultRedisUrl)

  val RedisLocationUri = new URI(RedisUrl)
  val RedisSocketAddr = new InetSocketAddress(RedisLocationUri.getHost, RedisLocationUri.getPort)

  /**
   * The format for `userInfo` is "[username]:[password]" but sending username
   * is unnecessary both locally (no auth) and for Heroku.
   */
  def redisClient(implicit actorSys: ActorSystem, ec: ExecutionContext): RedisClient = {
    val redisUri = new URI(RedisUrl)
    val password = Option(redisUri.getUserInfo).flatMap(_.split(":").lastOption)
    RedisClient(
      host = redisUri.getHost,
      port = redisUri.getPort,
      password = password
    )
  }

  def redisActorPublisher(implicit actorSys: ActorSystem): ActorRef = {
    actorSys.actorOf(
      Props(classOf[RedisActorPublisher], RedisModule.RedisSocketAddr)
        .withDispatcher("rediscala.rediscala-client-worker-dispatcher")
    )
  }
}
