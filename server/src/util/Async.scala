package monarchy.util

import scala.concurrent.{ExecutionContext, Future}

object Async {
  val None = Future.successful(scala.None)

  def join[A, B](f0: Future[A], f1: Future[B])(implicit ec: ExecutionContext): Future[(A, B)] = {
    f0.zip(f1)
  }
}
