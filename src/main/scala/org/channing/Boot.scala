package org.channing

import doobie.util.transactor.{DriverManagerTransactor, Transactor}

import scalaz.concurrent.Task

/**
  * This represents a main, something that instantiates your system
  * based on properties loaded etc.
  */
object Boot {

  def main(args: Array[String]): Unit = {
    val transactor: Transactor[Task] = DriverManagerTransactor[Task]("org.postgresql.Driver", "jdbc:postgresql:world", "postgres", "")
    val store = new DoobieStore(transactor)
    val service = new SomethingUsingTheStore(store)
  }
}
