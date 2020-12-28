package omnia.data.structure.mutable

import omnia.data.structure.Graph

interface MutableGraph<E> : Graph<E> {
  fun addNode(item: E)
  fun replaceNode(original: E, replacement: E)
  fun removeNode(item: E): Boolean {
    return removeUnknownTypedNode(item)
  }

  fun removeUnknownTypedNode(item: Any?): Boolean
}