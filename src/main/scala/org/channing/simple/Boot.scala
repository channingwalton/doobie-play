package org.channing.simple

import doobie.util.iolite.IOLite
import doobie.util.transactor.{DriverManagerTransactor, Transactor}

/**
  * This represents something that instantiates your system
  * based on properties loaded etc.
  */
object Boot {

  def main(args: Array[String]): Unit = {

    val transactor: Transactor[IOLite] = DriverManagerTransactor[IOLite]("org.h2.Driver", "jdbc:h2:/tmp/foo;TRACE_LEVEL_SYSTEM_OUT=2", "sa", "")

    val store = new DoobieStore(transactor)

    println(s"Schema created: " + store.runStoreIO(store.createSchema()))

    val layer1 = new Layer1(store)

    val service = new Service(layer1)

    val serviceOp = service.greatComplexService

    val result: Either[Throwable, String] = store.runStoreIO(serviceOp)

    println(s"Result: $result")
  }
}
