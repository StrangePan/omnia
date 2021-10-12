package omnia.data.structure.observable.writable

import omnia.data.contract.Writable
import omnia.data.structure.mutable.MutableSet
import omnia.data.structure.observable.ObservableSet

interface WritableObservableSet<E : Any> : MutableSet<E>, ObservableSet<E>, Writable<ObservableSet<E>> {
  companion object {

    @JvmStatic
    fun <E : Any> create(): WritableObservableSet<E> {
      return WritableObservableSetImpl()
    }
  }
}