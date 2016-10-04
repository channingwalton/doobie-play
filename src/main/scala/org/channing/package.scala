package org

import scalaz.WriterT

package object channing {
  type Work[C[_], A] = WriterT[C, List[PostCommit], A]
}
