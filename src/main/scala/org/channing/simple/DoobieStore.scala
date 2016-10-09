package org.channing.simple

import doobie.imports._
import doobie.util.transactor.Transactor

import scalaz.concurrent.Task
import scalaz.syntax.monad._
import scalaz.{Monad, WriterT, \/}

class DoobieStore(transactor: Transactor[Task]) extends Store[ConnectionIO] {

  val M: Monad[ConnectionIO] = implicitly[Monad[ConnectionIO]]

  def get(k: String): StoreIO[ConnectionIO, Option[String]] =
    WriterT[ConnectionIO, List[PostCommit], Option[String]](getDoobie(k).map(v ⇒ (Nil, v)))

  def put(k: String, v: String): StoreIO[ConnectionIO, Int] =
    WriterT[ConnectionIO, List[PostCommit], Int](putDoobie(k, v).map(v ⇒ (Nil, v)))

  def postCommit(pc: PostCommit): StoreIO[ConnectionIO, Unit] =
    WriterT[ConnectionIO, List[PostCommit], Unit]((List(pc), ()).point[ConnectionIO])

  private def getDoobie(k: String): ConnectionIO[Option[String]] =
    sql"select x from y".query[String].option

  private def putDoobie(k: String, v: String): ConnectionIO[Int] =
    sql"insert into person (name, age) values ($k, $v)".update.run

  def runStoreIO[T](storeIO: StoreIO[ConnectionIO, T]): Throwable \/ T = {
    storeIO.run.transact(transactor).attemptRun.map {
      case (postCommits, result) ⇒
        postCommits.foreach(_.f())
        result
    }
  }
}