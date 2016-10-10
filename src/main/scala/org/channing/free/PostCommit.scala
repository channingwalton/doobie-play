package org.channing.free

final case class PostCommit(f: () => Unit)