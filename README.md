# doobie-play

When using doobie in a real system you will probably want to
abstract away from ConnectionIO, a Doobie type, so it
doesn't leak out all over your codebase.

The project has a simple Store described by trait
parameterised by C[_], some context containing the result
of operations in the store.

To support the idea side-effects performed after a
successful store operation, a post commit, the operations
on Store return a StoreIO[C[_], A], a type alias for
WriterT[C, List[PostCommit], A]. This enables post commit
operations to be freely mixed in with store operations. 

See Store and SomethingUsingTheStore first, then see how
the DoobieStore is implemented.