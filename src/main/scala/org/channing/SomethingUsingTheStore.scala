package org.channing

import scalaz.Monad

// import the type class goodies so that
// we can use for-comprehensions to work
// with the store regardless of the store implementation
import StoreIO._

class SomethingUsingTheStore[C[_]: Monad](store: Store[C]) {

  def doIt: StoreIO[C, String] = {
    for {
      x ← store.get("a")
      y ← store.get("b")
      _ ← store.postCommit(PostCommit(() ⇒ println("nasty side-effect")))
    } yield s"$x + $y"
  }

  def executeIt(): Unit =
    store.runStoreIO(doIt).fold(_.printStackTrace, println)
}
