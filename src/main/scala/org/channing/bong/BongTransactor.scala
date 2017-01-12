package org.channing.bong

import cats.implicits._

import java.sql.Connection

import doobie.free.connection.ConnectionIO
import doobie.imports._
import fs2.util.{Catchable, Suspendable}

class BongTransactor[M[_]: Catchable : Suspendable] extends Transactor[M] {

  def postCommit(f: () ⇒ Unit): ConnectionIO[Unit] = HC.delay(f())

  override protected def after: ConnectionIO[Unit] =
    super.after *> super.after

  override protected def connect: M[Connection] = ???
}
