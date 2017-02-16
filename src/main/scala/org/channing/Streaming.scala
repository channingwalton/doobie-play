package org.channing

import doobie.imports._
import doobie.util.iolite.IOLite
import doobie.util.transactor.{DriverManagerTransactor, Transactor}
import cats.syntax.traverse._
import cats.instances.all._
import doobie.free.connection.ConnectionIO

object Streaming extends App {

  val dbFileName = "/tmp/streaming" + System.currentTimeMillis()

  val transactor: Transactor[IOLite] = DriverManagerTransactor[IOLite]("org.h2.Driver", s"jdbc:h2:$dbFileName;TRACE_LEVEL_SYSTEM_OUT=2", "sa", "")

  val createSchema: ConnectionIO[Int] = sql"create table KV (k VARCHAR2(10), v VARCHAR2(10)); create table stats (k VARCHAR2(10), v VARCHAR2(10));".update.run

  def putKV(k: String, v: String): ConnectionIO[Unit] =
    sql"insert into KV (k, v) values ($k, $v)".update.run.map(_ ⇒ ())

  def putStats(k: String, v: String): ConnectionIO[Unit] =
    sql"insert into stats (k, v) values ($k, $v)".update.run.map(_ ⇒ ())

  val getAllKV: Query0[(String, String)] = sql"select k, v from KV".query[(String, String)]

  /**
    * Get a stream of getAllKV and sink it into putStats
    */
  val mkStats: ConnectionIO[Unit] = getAllKV.process.buffer(3).sink(v ⇒ putStats(v._1, v._2))

  val getAllStats: ConnectionIO[Vector[(String, String)]] = sql"select k, v from stats".query[(String, String)].vector

  // run all the things
  createSchema.transact(transactor).unsafePerformIO

  val fill = (0 until 10).toList.map(v ⇒ putKV(v.toString, v.toString)).sequence
  fill.transact(transactor).unsafePerformIO

  println("**********************************")
  mkStats.transact(transactor).unsafePerformIO
  println("**********************************")

  getAllStats.transact(transactor).unsafePerformIO.foreach(println)

  println("All done")
}