package omnia.data.structure.observable.writable

import omnia.data.contract.Writable
import omnia.data.structure.mutable.MutableList
import omnia.data.structure.observable.ObservableList

interface WritableObservableList<E> : MutableList<E>, ObservableList<E>, Writable<ObservableList<E>> {
  companion object {
    fun <E> create(): WritableObservableList<E> {
      return WritableObservableListImpl()
    }
  }
}