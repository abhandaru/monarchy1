package monarchy.game

import scala.collection.mutable

object GraphOperations {
  def reachableFrom[T](n0: T, neighbors: T => Set[T]): Set[T] = {
    val visited = mutable.Set.empty[T]
    val queue = mutable.Queue(n0)
    while (queue.size > 0) {
      val n = queue.dequeue
      val ns = neighbors(n).filterNot(visited)
      visited.add(n)
      queue.enqueue(ns.toSeq: _*)
    }
    visited.toSet
  }
}
