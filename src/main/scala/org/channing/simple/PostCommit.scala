package org.channing.simple

final case class PostCommit(f: () => Unit)