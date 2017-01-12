package org.channing.simple

// import the type class goodies so that
// we can use for-comprehensions to work
// with the store regardless of the store implementation
import cats.Monad
import org.channing.simple.StoreIO._

class Layer1[C[_]: Monad](store: Store[C]) {

  def putThings: StoreIO[C, Unit] =
    for {
      _ ← store.put("a", "AA")
      _ ← store.put("b", "BB")
    } yield ()

  def getTheThings: StoreIO[C, String] = {
    for {
      x ← store.get("a")
      y ← store.get("b")
      _ ← store.postCommit(PostCommit(() ⇒ println("post commit side-effect")))
    } yield s"$x + $y"
  }

  def executeIt(): Unit =
    store.runStoreIO(getTheThings).fold(_.printStackTrace, println)
}
