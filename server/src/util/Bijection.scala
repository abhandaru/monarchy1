package monarchy.util

import java.util.Base64

trait Bijection[A, B] extends (A => B) { self =>
  def invert(b: B): A
  def inverse: Bijection[B, A] = new Bijection[B, A] {
    override def apply(b: B) = self.invert(b)
    override def invert(a: A) = self.apply(a)
    override def inverse = self
  }
}

trait StringBijection[T] extends Bijection[T, String]

trait BytesBijection[T] extends Bijection[T, Array[Byte]]

object BytesBijection {
  implicit object StringImpl extends BytesBijection[String] {
    override def apply(a: String): Array[Byte] = a.getBytes
    override def invert(b: Array[Byte]): String = new String(b)
  }
}

trait Base64Bijection[T] extends Bijection[T, String]

object Base64Bijection {
  def apply[A](implicit m: BytesBijection[A]): Base64Bijection[A] = {
    new Base64Bijection[A] {
      override def apply(a: A): String =
        Base64.getEncoder.encodeToString(m(a))

      override def invert(s: String): A =
        m.invert(Base64.getDecoder.decode(s))
    }
  }
}
