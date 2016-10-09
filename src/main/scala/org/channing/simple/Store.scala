package org.channing.simple

import scalaz.\/

abstract class Store[C[_]] {

  def runStoreIO[T](storeIO: StoreIO[C, T]): Throwable \/  T

  def get(k: String): StoreIO[C, Option[String]]

  def put(k: String, v: String): StoreIO[C, Int]

  def postCommit(pc: PostCommit): StoreIO[C, Unit]
}