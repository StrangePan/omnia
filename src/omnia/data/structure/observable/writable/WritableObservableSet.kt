package omnia.data.structure.observable.writable

import omnia.data.contract.Writable
import omnia.data.structure.mutable.MutableSet
import omnia.data.structure.observable.ObservableSet

interface WritableObservableSet<E> : MutableSet<E>, ObservableSet<E>, Writable<ObservableSet<E>> {
  companion object {

    @kotlin.jvm.JvmStatic
    fun <E> create(): WritableObservableSet<E> {
      return WritableObservableSetImpl()
    }
  }
}