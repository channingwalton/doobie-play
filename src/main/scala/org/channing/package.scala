package org

import doobie.imports.ConnectionIO

import scalaz.WriterT

package object channing {
  type Work[A] = WriterT[ConnectionIO, List[PostCommit], A]
}
