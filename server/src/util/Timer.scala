package monarchy.util

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object Timer {
  type Logger[A] = Timing[A] => Unit

  // Convenient method for clocking any future.
  def clock[A](gen: => Future[A])(log: Logger[A])(implicit ec: ExecutionContext): Future[A] = {
    val start = System.currentTimeMillis
    val result = gen
    result.onComplete { r => log(Timing(r, start, System.currentTimeMillis)) }
    result
  }

  case class Timing[A](
      result: Try[A],
      start: Long,
      end: Long
  ) {
    def duration: Long =
      end - start
  }
}
