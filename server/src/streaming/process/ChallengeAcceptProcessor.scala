package monarchy.streaming.process

import monarchy.dal
import monarchy.dalwrite.WriteQueryBuilder
import monarchy.{game => gg}
import monarchy.streaming.core._
import monarchy.util.{Async, Json}
import redis.RedisClient
import scala.util.Random
import scala.concurrent.{Future, ExecutionContext}

/**
 * TODO (adu): Actually solve this with some sort of mutex implementation
 * Partial impl: https://gist.github.com/abhandaru/712d4f2bfd360fe29b7ecefa81ac9c3f
 *
 * The async read/presence check then write below should be fast enough to avoid
 * most issues. We can move to something more correct as we bring on traffic.
 */
class ChallengeAcceptProcessor(implicit
  ec: ExecutionContext,
  redisCli: RedisClient,
  queryCli: dal.QueryClient
) extends ClientActionProcessor[ChallengeAccept] {
  import ChallengeAcceptProcessor._
  import dal.PostgresProfile.Implicits._

  override def apply(axn: ChallengeAccept): Future[_] = {
    val ChallengeAccept(auth, body) = axn
    val userId = auth.userId
    val opponentUserId = body.opponentId.toLong
    if (userId == opponentUserId) {
      Async.Unit
    } else {
      val challengeKey = StreamingKey.Challenge(userId).toString
      val opponentChallengeKey = StreamingKey.Challenge(opponentUserId).toString
      redisCli.get[Boolean](opponentChallengeKey).flatMap {
        case None => Async.Unit
        case Some(false) => Async.Unit
        case Some(true) =>
          Async.join(
            redisCli.del(challengeKey, opponentChallengeKey),
            redisCli.publish(StreamingChannel.Matchmaking, Json.stringify(Matchmaking(true))),
            redisCli.publish(StreamingChannel.gameCreate(userId), Json.stringify(GameCreate(77L))),
            redisCli.publish(StreamingChannel.gameCreate(opponentUserId), Json.stringify(GameCreate(77L)))
          ).map { case (count, _,  _, _) =>
            println(s"removed $count challenges on $challengeKey, $opponentChallengeKey and accept")
          }
      }
    }
  }

  def create(userIds: Seq[Long]): Future[Long] = {
    for {
      users <- queryCli.all(dal.User.query.filter(_.id inSet userIds))
      gameNode <- createGame(users)
    } yield {
      gameNode.value.id
      // val gameLive = gg.GameBuilder(
      //   seed = gg.seed,
      //   players = gg.players.map { player =>
      //     gg.Player(
      //       playerId = gg.PlayerId(player.id),
      //       formation = DefaultFormation
      //     )
      //   }
      // )
    }
  }

  def createGame(users: Seq[dal.User]): Future[GameNode] = {
    val seed = System.currentTimeMillis.toInt
    val game = dal.Game(seed = seed, status = dal.GameStatus.Started)
    val query = for {
      gameWr <- WriteQueryBuilder.put(game)
      playersWr <- WriteQueryBuilder.putAll(
        users.map { user =>
          dal.Player(
            gameId = gameWr.id,
            userId = user.id,
            status = dal.PlayerStatus.Absent,
            rating = user.rating
          )
        }
      )
    } yield {
      GameNode(
        value = gameWr,
        players = playersWr
      )
    }
    queryCli.write(query)
  }
}

object ChallengeAcceptProcessor {
  val DefaultFormation: gg.Player.Formation = {
    Seq(
      gg.Vec(5, 4) -> gg.Knight,
      gg.Vec(4, 3) -> gg.Pyromancer,
      gg.Vec(6, 3) -> gg.Scout,
      gg.Vec(5, 2) -> gg.Cleric
    )
  }
}

case class GameNode(
  value: dal.Game,
  players: Seq[dal.Player]
)
