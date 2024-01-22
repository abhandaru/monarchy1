package monarchy.util

import scala.concurrent.{ExecutionContext, Future}

object Async {
  val None = Future.successful(scala.None)
  val Unit = Future.successful(())
  val Zero = Future.successful(0)

  def apply[A](value: A): Future[A] =
    Future.successful(value)

  def join[A, B](f0: Future[A], f1: Future[B])(implicit ec: ExecutionContext): Future[(A, B)] =
    f0.zip(f1)

  def join[A, B, C](f0: Future[A], f1: Future[B], f2: Future[C])(implicit ec: ExecutionContext): Future[(A, B, C)] =
    f0.zip(f1).zip(f2).map { case ((r0, r1), r2) => (r0, r1, r2) }

  def join[A, B, C, D](f0: Future[A], f1: Future[B], f2: Future[C], f3: Future[D])(implicit ec: ExecutionContext): Future[(A, B, C, D)] =
    f0.zip(f1).zip(f2).zip(f3).map { case (((r0, r1), r2), r3) => (r0, r1, r2, r3) }
}
