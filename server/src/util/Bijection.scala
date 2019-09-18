package monarchy.util

trait Bijection[A, B] extends (A => B) { self =>
  def invert(b: B): A
  def inverse: Bijection[B, A] = new Bijection[B, A] {
    override def apply(b: B) = self.invert(b)
    override def invert(a: A) = self.apply(a)
    override def inverse = self
  }
}

trait StringBijection[T] extends Bijection[T, String]
