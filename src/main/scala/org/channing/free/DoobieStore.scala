package org.channing.free

import cats._
import cats.data.{WriterT, Xor}
import doobie.imports._
import doobie.util.iolite.IOLite
import doobie.util.transactor.Transactor
import org.channing.free.Store._
import org.channing.simple.StoreIO

class DoobieStore(transactor: Transactor[IOLite]) {

  type DoobieStored[T] = WriterT[ConnectionIO, List[PostCommit], T]

  def dbInterpreter: KVStoreA ~> DoobieStored =
    new (KVStoreA ~> DoobieStored) {
      def apply[A](fa: KVStoreA[A]): DoobieStored[A] =
        fa match {
          case Put(key, value) =>
            WriterT[ConnectionIO, List[PostCommit], Unit](putDoobie(key, value).map(v ⇒ (Nil, ())))
          case Get(key) =>
            WriterT[ConnectionIO, List[PostCommit], Option[String]](getDoobie(key).map(v ⇒ (Nil, v)))
        }
    }

  private def getDoobie(k: String): ConnectionIO[Option[String]] =
    sql"select x from y".query[String].option

  private def putDoobie(k: String, v: String): ConnectionIO[Unit] =
    sql"insert into person (name, age) values ($k, $v)".update.run.map(_ ⇒ ())

  def runStoreIO[T](storeIO: StoreIO[ConnectionIO, T]): Throwable Xor T = {
    storeIO.run.transact(transactor).attempt.map { either ⇒
      Xor.fromEither(either).map {
        case (postCommits, result) ⇒
          postCommits.foreach(_.f())
          result
      }
    }
  }.unsafePerformIO
}
