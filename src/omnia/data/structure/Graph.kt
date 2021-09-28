package omnia.data.structure

interface Graph<E : Any> {

  interface Node<E : Any> {

    fun item(): E

    fun edges(): Set<out Edge<E>>

    fun neighbors(): Set<out Node<E>>
  }

  interface Edge<E : Any> {

    fun endpoints(): Collection<out Node<E>>
  }

  fun nodeOf(item: E): Node<E>?

  fun nodeOfUnknownType(item: Any?): Node<E>?

  fun contents(): Set<E>

  fun nodes(): Set<out Node<E>>

  fun edges(): Set<out Edge<E>>
}