package monarchy.util

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object Timer {
  // Convenient method for clocking any future.
  def clock[T](gen: => Future[T])(log: (Long, Long) => Unit)(
      implicit ec: ExecutionContext
  ): Future[T] = {
    val start = System.currentTimeMillis
    val result = gen
    result.onComplete { case _ => log(start, System.currentTimeMillis) }
    result
  }
}
