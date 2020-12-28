package omnia.data.structure.mutable

import omnia.data.structure.UndirectedGraph

interface MutableUndirectedGraph<E> : MutableGraph<E>, UndirectedGraph<E> {
  fun addEdge(first: E, second: E)
  fun removeEdge(first: E, second: E): Boolean
}