package org.channing.simple

import cats.implicits._
import cats.data.WriterT

import doobie.imports._
import doobie.util.transactor.Transactor
import doobie.util.iolite.IOLite

class DoobieStore(transactor: Transactor[IOLite]) extends Store[ConnectionIO] {

  def get(k: String): StoreIO[ConnectionIO, Option[String]] =
    WriterT[ConnectionIO, List[PostCommit], Option[String]](getDoobie(k).map(v ⇒ (Nil, v)))

  def put(k: String, v: String): StoreIO[ConnectionIO, Int] =
    WriterT[ConnectionIO, List[PostCommit], Int](putDoobie(k, v).map(v ⇒ (Nil, v)))

  def postCommit(pc: PostCommit): StoreIO[ConnectionIO, Unit] =
    WriterT[ConnectionIO, List[PostCommit], Unit]((List(pc), ()).pure[ConnectionIO])

  private def getDoobie(k: String): ConnectionIO[Option[String]] =
    sql"select v from KV where k=$k".query[String].option

  private def putDoobie(k: String, v: String): ConnectionIO[Int] =
    sql"insert into KV (k, v) values ($k, $v)".update.run

  def createSchema(): StoreIO[ConnectionIO, Int] =
    WriterT[ConnectionIO, List[PostCommit], Int](create.map(v ⇒ (Nil, v)))

  private def create: ConnectionIO[Int] = sql"create table KV (k VARCHAR2(10), v VARCHAR2(10));".update.run

  def delay(f: () ⇒ Unit): StoreIO[ConnectionIO, Unit] =
    WriterT[ConnectionIO, List[PostCommit], Unit](HC.delay(f()).map(v ⇒ (Nil, v)))

  def runStoreIO[T](storeIO: StoreIO[ConnectionIO, T]): Throwable Either T = {
    storeIO.run.transact(transactor).attempt.map { either ⇒
      either.map {
        case (postCommits, result) ⇒
          println("Running post commits")
          postCommits.foreach(_.f())
          result
      }
    }
  }.unsafePerformIO
}
