package org.channing

class SomethingUsingTheStore[C[_]](store: Store[C]) {

  // import the type class goodies in store so that
  // we can use for-comprehensions to work
  // with the store regardless of the store implementation
  import store._

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
