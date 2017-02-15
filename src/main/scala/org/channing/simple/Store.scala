package org.channing.simple

abstract class Store[C[_]] {

  def runStoreIO[T](storeIO: StoreIO[C, T]): Throwable Either  T

  def get(k: String): StoreIO[C, Option[String]]

  def put(k: String, v: String): StoreIO[C, Int]

  def postCommit(pc: PostCommit): StoreIO[C, Unit]

  def createSchema(): StoreIO[C, Int]

  def delay(f: () ⇒ Unit): StoreIO[C, Unit]
}