package org.channing.free

import cats.free.Free
import cats.free.Free.liftF

object Store {

  sealed trait KVStoreA[A]

  case class Put(key: String, value: String) extends KVStoreA[Unit]

  case class Get(key: String) extends KVStoreA[Option[String]]

  type KVStore[A] = Free[KVStoreA, A]

  // Put returns nothing (i.e. Unit).
  def put(key: String, value: String): KVStore[Unit] =
    liftF[KVStoreA, Unit](Put(key, value))

  // Get returns a String.
  def get(key: String): KVStore[Option[String]] =
    liftF[KVStoreA, Option[String]](Get(key))

  // Update composes get and set, and returns nothing.
  def update(key: String, f: String => String): KVStore[Unit] =
    for {
      vMaybe <- get(key)
      _ <- vMaybe.map(v => put(key, f(v))).getOrElse(Free.pure(()))
    } yield ()
}