package org.channing

import scalaz.{Monad, \/}

abstract class Store[C[_]] {

  implicit def M: Monad[C]

  def runWork[T](work: Work[C, T]): Throwable \/  T

  def get(k: String): Work[C, Option[String]]

  def put(k: String, v: String): Work[C, Int]

  def postCommit(pc: PostCommit): Work[C, Unit]
}