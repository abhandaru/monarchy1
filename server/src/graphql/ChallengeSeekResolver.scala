package monarchy.graphql

import java.time.Instant
import java.util.UUID
import monarchy.dal
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import scala.concurrent.duration._

object Challenge {
  case class Data(
      host: dal.User,
      expireAt: Instant,
  )
}

object ChallengeSeekResolver extends Resolver[Unit, Challenge.Data] {
  import dal.PostgresProfile.Implicits._
  val TtlSeconds = 300

  override def apply(in: In): Out = {
    // Get all the implicits
    import in.ctx._
    val userId = expectUserId(in)
    val expireAt = Instant.now.plusSeconds(TtlSeconds)
    Async.join(
      queryCli.first(dal.User.query.filter(_.id === userId)),
      redisCli.set(StreamingKey.Challenge(userId), "true", exSeconds = Some(TtlSeconds)),
      redisCli.publish(StreamingChannel.Matchmaking, Json.stringify(Matchmaking(true))),
    ).map {
      case (None, _, _) => throw NotFound(s"user $userId")
      case (Some(host), _, _) => Challenge.Data(host, expireAt)
    }
  }
}
