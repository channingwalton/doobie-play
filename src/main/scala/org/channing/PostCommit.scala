package org.channing

final case class PostCommit(f: () => Unit)