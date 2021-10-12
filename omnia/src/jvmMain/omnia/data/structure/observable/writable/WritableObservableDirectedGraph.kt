package omnia.data.structure.observable.writable

import omnia.data.structure.DirectedGraph
import omnia.data.structure.mutable.MutableDirectedGraph
import omnia.data.structure.observable.ObservableDirectedGraph

interface WritableObservableDirectedGraph<E : Any>
  : MutableDirectedGraph<E>, ObservableDirectedGraph<E>, WritableObservableGraph<E> {

  override fun toReadOnly(): ObservableDirectedGraph<E>

  companion object {

    @JvmStatic
    fun <E : Any> create(): WritableObservableDirectedGraph<E> {
      return WritableObservableDirectedGraphImpl()
    }

    fun <E : Any> copyOf(original: DirectedGraph<E>): WritableObservableDirectedGraph<E> {
      return WritableObservableDirectedGraphImpl(original)
    }
  }
}