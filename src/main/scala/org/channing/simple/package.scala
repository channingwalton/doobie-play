package org.channing

import scalaz.WriterT

package object simple {
  type StoreIO[C[_], A] = WriterT[C, List[PostCommit], A]
}
