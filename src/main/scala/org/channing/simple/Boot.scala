package org.channing.simple

import doobie.util.iolite.IOLite
import doobie.util.transactor.{DriverManagerTransactor, Transactor}

/**
  * This represents something that instantiates your system
  * based on properties loaded etc.
  */
object Boot {

  def main(args: Array[String]): Unit = {
    val transactor: Transactor[IOLite] = DriverManagerTransactor[IOLite]("org.postgresql.Driver", "jdbc:postgresql:world", "postgres", "")
    val store = new DoobieStore(transactor)
    val something = new SomethingUsingTheStore(store)
    val service = new Service(something)
  }
}
