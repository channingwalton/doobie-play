package org.channing.free

import cats.free.Free
import cats.free.Free._
import cats.~>

object Service {
  type ServiceOp[A] = Free[ServiceOpA, A]

  sealed trait ServiceOpA[A]

  object GreatComplexService extends ServiceOpA[String]

  def greatComplexService: ServiceOp[String] =
    liftF[ServiceOpA, String](GreatComplexService)

  import Layer1._
  def layerInterpreter: ServiceOpA ~> L1Op =
    new (ServiceOpA ~> L1Op) {
      def apply[A](fa: ServiceOpA[A]): L1Op[A] =
        fa match {
          case GreatComplexService =>
            for {
              a ← Layer1.op1("hi")
              b ← Layer1.op1("bye")
            } yield a + b
        }
    }
}
