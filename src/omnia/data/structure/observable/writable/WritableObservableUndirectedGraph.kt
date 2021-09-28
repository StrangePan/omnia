package omnia.data.structure.observable.writable

import omnia.data.structure.mutable.MutableUndirectedGraph
import omnia.data.structure.observable.ObservableUndirectedGraph

interface WritableObservableUndirectedGraph<E : Any> :
    MutableUndirectedGraph<E>, ObservableUndirectedGraph<E>, WritableObservableGraph<E> {

  override fun toReadOnly(): ObservableUndirectedGraph<E>
}