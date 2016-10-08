package org.channing

import scalaz.{Monad, Semigroup, \/}
import shapeless.cachedImplicit

import scalaz.std.list._

abstract class Store[C[_]] {

  // cache these implicits so users of the store don't need to import them
  // which is often confusing
  // users should import store._

  // this implicit summons the Monad for List
  implicit val listMonad: Monad[List] = cachedImplicit

  // this implicit summons the Semigroup for List[PostCommit]
  // which is required because StoreIO is a Writer whose
  // written value is a List[PostCommit]
  implicit val appMonad: Semigroup[List[PostCommit]] = cachedImplicit

  implicit def M: Monad[C]

  def runStoreIO[T](storeIO: StoreIO[C, T]): Throwable \/  T

  def get(k: String): StoreIO[C, Option[String]]

  def put(k: String, v: String): StoreIO[C, Int]

  def postCommit(pc: PostCommit): StoreIO[C, Unit]
}