package org.channing.free

import cats.data.Xor
import doobie.imports.{IOLite, Transactor}
import doobie.util.transactor.DriverManagerTransactor
import org.channing.free.Store.KVStore

object Boot {

  def main(args: Array[String]): Unit = {
    val transactor: Transactor[IOLite] = DriverManagerTransactor[IOLite]("org.postgresql.Driver", "jdbc:postgresql:world", "postgres", "")

    val store = new DoobieStore(transactor)
    import store.DoobieStoredMonad

    // perform an operation at the service level
    val serviceOp = Service.greatComplexService

    // transform it into layer1 ops
    val layerOps = serviceOp.foldMap(Service.layerInterpreter)

    // and then to store ops
    val opAsStore: KVStore[String] = layerOps.foldMap(Layer1.storeInterpreter)

    // and then as doobie ops
    val workToDo: store.DoobieStored[String] = opAsStore.foldMap(store.dbInterpreter)

    // run it to get a result
    val res: Xor[Throwable, String] = store.runStoreIO(workToDo)
  }
}
