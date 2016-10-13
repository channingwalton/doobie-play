package org.channing.free

import cats.data.Xor
import doobie.imports.{IOLite, Transactor}
import doobie.util.transactor.DriverManagerTransactor
import org.channing.free.Layer1.L1Op
import org.channing.free.Store.KVStore

object Boot {

  def main(args: Array[String]): Unit = {
    val transactor: Transactor[IOLite] = DriverManagerTransactor[IOLite]("org.postgresql.Driver", "jdbc:postgresql:world", "postgres", "")

    val store = new DoobieStore(transactor)
    import store.DoobieStoredMonad

    val op: L1Op[String] = Layer1.op1("hello")

    val opAsStore: KVStore[String] = op.foldMap(Layer1.storeInterpreter)

    val workToDo: store.DoobieStored[String] = opAsStore.foldMap(store.dbInterpreter)

    val res: Xor[Throwable, String] = store.runStoreIO(workToDo)
  }
}
