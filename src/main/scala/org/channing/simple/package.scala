package org.channing

import cats.data.WriterT

package object simple {
  type StoreIO[C[_], A] = WriterT[C, List[PostCommit], A]
}
