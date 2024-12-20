package monarchy.streaming.format

import java.util.UUID
import monarchy.dal
import monarchy.streaming.core.{Matchmaking, StreamingKey}
import redis.RedisClient
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object MatchmakingRenderer {
  case class User(id: String, username: String, rating: Int)
  case class Data(host: User)
}

class MatchmakingRenderer(implicit
    ec: ExecutionContext,
    queryCli: dal.QueryClient,
    redisCli: RedisClient
) extends ActionRenderer.Impl[Matchmaking] {
  import dal.PostgresProfile.Implicits._
  import MatchmakingRenderer._

  override def renderOpt(axn: Matchmaking): Future[Option[_]] =
    render(axn).map { r => Some(r) }

  private def render(axn: Matchmaking): Future[Seq[Data]] = {
    val fetchUsers = for {
      cursor <- redisCli.scan(cursor = 0, matchGlob = Some(StreamingKey.ChallengeScan))
      userIds = for {
        key <- cursor.data
        userIdRaw = key.stripPrefix(StreamingKey.ChallengeScan.prefix)
        userId <- Try(UUID.fromString(userIdRaw)).toOption.toSeq
      } yield userId
      users <- queryCli.all(dal.User.query.filter(_.id inSet userIds))
    } yield users
    fetchUsers.map { users =>
      users.map { user =>
        Data(User(
          id = user.id.toString,
          username = user.username,
          rating = user.rating
        ))
      }
    }
  }
}
