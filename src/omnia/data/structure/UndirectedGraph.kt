package omnia.data.structure

import java.util.Optional

interface UndirectedGraph<E> : Graph<E> {
    interface UndirectedNode<E> : Graph.Node<E> {
        override fun edges(): Set<out UndirectedEdge<E>>
        override fun neighbors(): Set<out UndirectedNode<E>>
    }

    interface UndirectedEdge<E> : Graph.Edge<E> {
        override fun endpoints(): Collection<out UndirectedNode<E>>
    }

    override fun nodeOf(item: E): Optional<out UndirectedNode<E>>
    override fun nodeOfUnknownType(item: Any?): Optional<out UndirectedNode<E>>
    fun edgeOf(first: E, second: E): Optional<out UndirectedEdge<E>>
    override fun nodes(): Set<out UndirectedNode<E>>
    override fun edges(): Set<out UndirectedEdge<E>>
}