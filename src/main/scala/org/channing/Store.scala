package org.channing

import scalaz.{Monad, \/}
import StoreIO._

abstract class Store[C[_]] {

  implicit def M: Monad[C]

  def runStoreIO[T](storeIO: StoreIO[C, T]): Throwable \/  T

  def get(k: String): StoreIO[C, Option[String]]

  def put(k: String, v: String): StoreIO[C, Int]

  def postCommit(pc: PostCommit): StoreIO[C, Unit]
}