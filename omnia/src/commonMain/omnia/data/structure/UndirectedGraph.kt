package omnia.data.structure

interface UndirectedGraph<E : Any> : Graph<E> {

  interface UndirectedNode<E : Any> : Graph.Node<E> {

    override val edges: Set<out UndirectedEdge<E>>

    override val neighbors: Set<out UndirectedNode<E>>
  }

  interface UndirectedEdge<E : Any> : Graph.Edge<E> {

    override val endpoints: Collection<out UndirectedNode<E>>
  }

  override fun nodeOf(item: E): UndirectedNode<E>?

  override fun nodeOfUnknownType(item: Any?): UndirectedNode<E>?

  fun edgeOf(first: E, second: E): UndirectedEdge<E>?

  override val nodes: Set<out UndirectedNode<E>>

  override val edges: Set<out UndirectedEdge<E>>
}