package monarchy.streaming

import monarchy.dal
import redis.RedisClient
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class MatchmakingRenderer(implicit val ec: ExecutionContext, queryCli: dal.QueryClient, redisCli: RedisClient)
  extends ActionRenderer[Matchmaking] {
  import dal.PostgresProfile.Implicits._

  override def render(axn: Matchmaking): Future[_] = {
    val fetchUsers = for {
      cursor <- redisCli.scan(cursor = 0, matchGlob = Some(StreamingKey.ChallengeScan))
      userIds = for {
        key <- cursor.data
        userId <- Try(key.stripPrefix(StreamingKey.ChallengeScan.prefix).toLong).toOption.toSeq
      } yield userId
      users <- queryCli.all(dal.User.query.filter(_.id inSet userIds))
    } yield users
    fetchUsers.map { users =>
      users.map { user =>
        Map("userId" -> user.id, "username" -> user.username)
      }
    }
  }
}
