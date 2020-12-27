package omnia.data.structure.mutable

import omnia.data.structure.DirectedGraph

interface MutableDirectedGraph<E> : MutableGraph<E>, DirectedGraph<E> {
    fun addEdge(from: E, to: E)
    fun removeEdge(from: E, to: E): Boolean
    fun removeEdgeUnknownTyped(from: Any?, to: Any?): Boolean
}