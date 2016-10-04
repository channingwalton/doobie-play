package org.channing

import scalaz.std.list._

class SomethingElse[C[_]](store: Store[C]) {

  import store._

  def doIt(): Work[C, String] =
    for {
      x ← store.get("a")
      y ← store.get("b")
      _← store.postCommit(PostCommit(() ⇒ println("nasty side-effect")))
    } yield s"$x + $y"
}
