package omnia.data.structure

interface Graph<E : Any> {

  interface Node<E : Any> {

    val item: E

    val edges: Set<out Edge<E>>

    val neighbors: Set<out Node<E>>
  }

  interface Edge<E : Any> {

    val endpoints: Collection<out Node<E>>
  }

  fun nodeOf(item: E): Node<E>?

  fun nodeOfUnknownType(item: Any?): Node<E>?

  val contents: Set<E>

  val nodes: Set<out Node<E>>

  val edges: Set<out Edge<E>>
}