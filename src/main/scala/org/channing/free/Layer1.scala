package org.channing.free

import cats.free.Free
import cats.free.Free._
import cats.~>

object Layer1 {
  type L1Op[A] = Free[L1OpA, A]

  sealed trait L1OpA[A]

  case class Op1(arg: String) extends L1OpA[String]

  def op1(arg: String): L1Op[String] =
    liftF[L1OpA, String](Op1(arg))


  import Store._
  def storeInterpreter: L1OpA ~> KVStore =
    new (L1OpA ~> KVStore) {
      def apply[A](fa: L1OpA[A]): KVStore[A] =
        fa match {
          case Op1(arg) =>
            get(arg).map(_.getOrElse("whatever"))
        }
    }
}