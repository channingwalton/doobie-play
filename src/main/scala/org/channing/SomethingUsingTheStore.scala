package org.channing

class SomethingUsingTheStore[C[_]](store: Store[C]) {

  import store._

  type MyStoreIO[T] = StoreIO[C, T]

  def doIt: MyStoreIO[String] = {
    for {
      x ← store.get("a")
      y ← store.get("b")
      _ ← store.postCommit(PostCommit(() ⇒ println("nasty side-effect")))
    } yield s"$x + $y"
  }
}
