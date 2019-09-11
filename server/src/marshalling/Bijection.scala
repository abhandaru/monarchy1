package monarchy.marshalling

trait Bijection[A, B] extends (A => B) {
  def invert(b: B): A
}

trait JsonBijection[T] extends Bijection[T, String]
