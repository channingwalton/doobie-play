package org.channing

import shapeless.cachedImplicit

import scalaz.{Monad, Semigroup}
import scalaz.std.list._

object StoreIO {
  // cache these implicits so users of the store don't need to import them
  // which is often confusing
  // users should import StoreIO._

  // this implicit summons the Monad for List
  implicit val listMonad: Monad[List] = cachedImplicit

  // this implicit summons the Semigroup for List[PostCommit]
  // which is required because StoreIO is a Writer whose
  // written value is a List[PostCommit]
  implicit val appMonad: Semigroup[List[PostCommit]] = cachedImplicit

}
