package monarchy.game

import scala.collection.mutable

object GraphOperations {

  /**
   * NOTE: We use mutable datastructures here because they lend to much more
   * performant algorithms. Some of these graph operations are on hot paths
   * so it makes sense to optimize over idiomatic scala here.
   */
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
