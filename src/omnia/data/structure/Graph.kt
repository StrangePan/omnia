package omnia.data.structure

import java.util.Optional

interface Graph<E> {
  interface Node<E> {

    fun item(): E
    fun edges(): Set<out Edge<E>>
    fun neighbors(): Set<out Node<E>>
  }

  interface Edge<E> {

    fun endpoints(): Collection<out Node<E>>
  }

  fun nodeOf(item: E): Optional<out Node<E>>
  fun nodeOfUnknownType(item: Any?): Optional<out Node<E>>
  fun contents(): Set<E>
  fun nodes(): Set<out Node<E>>
  fun edges(): Set<out Edge<E>>
}