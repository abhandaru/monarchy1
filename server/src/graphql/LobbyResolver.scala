package monarchy.graphql

import java.time.Instant
import java.util.UUID
import monarchy.dal
import monarchy.streaming.core._
import monarchy.util.Async
import redis.api.Limit
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object Lobby {
  case class Data(
      online: Seq[dal.User],
      challenges: Seq[Challenge.Data],
  )
}

object LobbyResolver extends Resolver[Unit, Lobby.Data] {
  import dal.PostgresProfile.Implicits._
  val OnlineTtlMillis = 3 * 60 * 1000 // 3 minutes

  override def apply(in: In): Out = {
    import in.ctx._
    Async.join(
      fetchChallenges(in),
      fetchOnline(in)
    ).map { case (cs, us) => Lobby.Data(online = us, challenges = cs) }
  }

  private def fetchOnline(in: In): Future[Seq[dal.User]] = {
    import in.ctx._
    val t = Instant.now.toEpochMilli
    val threshold = Instant.now.toEpochMilli - OnlineTtlMillis
    for {
      userIdsRaw <- redisCli.zrangebyscore[String](StreamingKey.Online, mkLimit(threshold), mkLimit(t))
      userIds = userIdsRaw.map(UUID.fromString)
      users <- queryCli.all(dal.User.query.filter(_.id inSet userIds))
    } yield users
  }

  private def fetchChallenges(in: In): Future[Seq[Challenge.Data]] = {
    import in.ctx._
    for {
      cursor <- redisCli.scan(cursor = 0, matchGlob = Some(StreamingKey.ChallengeScan))
      userIds = for {
        key <- cursor.data
        userIdRaw = key.stripPrefix(StreamingKey.ChallengeScan.prefix)
        userId <- Try(UUID.fromString(userIdRaw)).toOption.toSeq
      } yield userId
      users <- queryCli.all(dal.User.query.filter(_.id inSet userIds))
    } yield users.map(mkChallenge)
  }

  private def mkChallenge(user: dal.User): Challenge.Data = {
    Challenge.Data(
      host = user,
      expireAt = None,
    )
  }

  private def mkLimit(millis: Long): Limit =
    Limit(millis.toDouble, inclusive = true)
}
