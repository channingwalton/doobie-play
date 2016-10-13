package org.channing.free

import cats.data.Xor
import cats.~>
import doobie.imports.{IOLite, Transactor}
import doobie.util.transactor.DriverManagerTransactor
import org.channing.free.Store.KVStoreA

object Boot {

  def main(args: Array[String]): Unit = {
    val transactor: Transactor[IOLite] = DriverManagerTransactor[IOLite]("org.postgresql.Driver", "jdbc:postgresql:world", "postgres", "")

    val store = new DoobieStore(transactor)

    val something = new SomethingUsingTheStore

    val service = new Service(something)

    val interpreter: KVStoreA ~> store.DoobieStored = store.dbInterpreter

    val workToDo = Store.get("").foldMap(interpreter)

    val res: Xor[Throwable, Option[String]] = store.runStoreIO(workToDo)
  }
}
