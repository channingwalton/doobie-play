package org.channing.free

import cats.data.Xor
import cats.~>
import cats.data.WriterT._

import doobie.imports.{IOLite, Transactor}
import doobie.util.transactor.DriverManagerTransactor
import org.channing.free.Store.KVStoreA

object Boot {

  def main(args: Array[String]): Unit = {
    val transactor: Transactor[IOLite] = DriverManagerTransactor[IOLite]("org.postgresql.Driver", "jdbc:postgresql:world", "postgres", "")

    val store = new DoobieStore(transactor)
    import store.DoobieStoredMonad

    val something = new SomethingUsingTheStore

    val service = new Service(something)

    val interpreter: KVStoreA ~> store.DoobieStored = store.dbInterpreter

    val workToDo: store.DoobieStored[String] = service.greatComplexService.foldMap(interpreter)

    val res: Xor[Throwable, String] = store.runStoreIO(workToDo)
  }
}
