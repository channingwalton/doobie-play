package org.channing

import doobie.imports._
import doobie.util.transactor.Transactor

import scalaz.{Monad, WriterT, \/}
import scalaz.effect.IO

class DoobieStore(transactor: Transactor[IO]) extends Store[ConnectionIO] {

  val M: Monad[ConnectionIO] = implicitly[Monad[ConnectionIO]]

  def get(k: String): Work[ConnectionIO, Option[String]] =
    WriterT[ConnectionIO, List[PostCommit], Option[String]](getDoobie(k).map(v ⇒ (Nil, v)))

  private def getDoobie(k: String): ConnectionIO[Option[String]] =
    sql"select x from y".query[String].option

  def runWork[T](work: Work[ConnectionIO, T]): Throwable \/  T =
    \/.fromTryCatchNonFatal {
    val conn: ConnectionIO[(List[PostCommit], T)] = work.run
    val r: (List[PostCommit], T) = conn.transact(transactor).unsafePerformIO()
    r._1.foreach(_.f())
    r._2
  }
}