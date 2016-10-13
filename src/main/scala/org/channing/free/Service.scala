package org.channing.free

import org.channing.free.Store.KVStore

class Service(somethingUsingTheStore: SomethingUsingTheStore) {

  def greatComplexService: KVStore[String] =
    for {
      a ← somethingUsingTheStore.doIt
      b ← somethingUsingTheStore.doIt
    } yield a + b
}
