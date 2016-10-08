package org

import scalaz.WriterT

package object channing {
  type StoreIO[C[_], A] = WriterT[C, List[PostCommit], A]
}
