package org.channing.free

import org.channing.free.Store.KVStore

class SomethingUsingTheStore {

  def doIt: KVStore[String] = {
    for {
      x ← Store.get("a")
      y ← Store.get("b")
    } yield s"$x + $y"
  }
}
