package monarchy.streaming.process

import monarchy.dal
import monarchy.dalwrite.{GameNode, WriteQueryBuilder}
import monarchy.game._
import monarchy.marshalling.GameJsonObjectMapper
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
            create(Seq(userId, opponentUserId))
          ).flatMap { case (count, _,  gameId) =>
            val channelEvent = Json.stringify(GameCreate(gameId))
            Async.join(
              redisCli.publish(StreamingChannel.gameCreate(userId), channelEvent),
              redisCli.publish(StreamingChannel.gameCreate(opponentUserId), channelEvent)
            ).map { case _ =>
              println(s"removed $count challenges on $challengeKey, $opponentChallengeKey and accept")
            }
          }
      }
    }
  }

  def create(userIds: Seq[Long]): Future[Long] = {
    for {
      users <- queryCli.all(dal.User.query.filter(_.id inSet userIds))
      game <- createGame(users)
      liveGame <- createLiveGame(game)
    } yield game.data.id
  }

  def createLiveGame(game: GameNode): Future[Boolean] = {
    val liveGame = GameBuilder(
      seed = game.data.seed,
      players = game.players.map { player =>
        Player(
          id = PlayerId(player.id),
          formation = DefaultFormation
        )
      }
    )
    val liveGameRep = Json.stringifyWith(GameJsonObjectMapper, liveGame)
    redisCli.set(StreamingKey.Game(game.data.id), liveGameRep)
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
            status = dal.PlayerStatus.Pending,
            rating = user.rating
          )
        }
      )
    } yield GameNode(data = gameWr, players = playersWr)
    queryCli.write(query)
  }
}

object ChallengeAcceptProcessor {
  val DefaultFormation: Player.Formation = {
    Seq(
      Vec(5, 4) -> Knight,
      Vec(4, 3) -> Pyromancer,
      Vec(6, 3) -> Scout,
      Vec(5, 2) -> Cleric
    )
  }
}
