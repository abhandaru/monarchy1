package monarchy.auth.oauth2

import java.time.Instant
import scala.collection.concurrent.TrieMap
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration

trait Store[A] {
  def get(key: String): Future[Option[A]]
  def set(key: String, value: A, ttl: FiniteDuration): Future[Boolean]
}

object Store {
  class InMemory[A] {
    import InMemory._
    private val cache = TrieMap.empty[String, Value[A]]

    def get(key: String): Future[Option[A]] = {
      val t = Instant.now()
      val value = cache.get(key).collect { case Value(v, exp) if exp.isAfter(t) => v }
      Future.successful(value)
    }

    def set(key: String, value: A, ttl: FiniteDuration): Future[Boolean] = {
      val t = java.time.Duration.ofSeconds(ttl.toSeconds)
      cache.update(key, Value(value, Instant.now.plus(t)))
      Future.successful(true)
    }
  }

  object InMemory {
    case class Value[A](value: A, expiresAt: Instant)
  }
}
