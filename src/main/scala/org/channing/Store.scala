package org.channing

import scalaz.{Monad, \/}

abstract class Store[C[_]] {

  implicit def M: Monad[C]

  def runWork[T](work: Work[C, T]): Throwable \/  T

  def get(k: String): Work[C, Option[String]]

}
