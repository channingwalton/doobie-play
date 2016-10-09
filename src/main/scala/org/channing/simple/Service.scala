package org.channing.simple

import org.channing.simple.StoreIO._

import scalaz.Monad

/**
  * This class does not directly make use of a Store, but works with
  * StoreIO[C, ?].
  *
  * To work with StoreIO in for comprehensions we require that C has a Monad
  * hence the type parameter is C[_]: Monad
  *
  * StoreIO._ is also imported since it has other implicits required for
  * working with StoreIO without having to remember what they are.
  */
class Service[C[_]: Monad](somethingUsingTheStore: SomethingUsingTheStore[C]) {

  def greatComplexService: StoreIO[C, String] =
    for {
      a ← somethingUsingTheStore.doIt
      b ← somethingUsingTheStore.doIt
    } yield a + b
}
