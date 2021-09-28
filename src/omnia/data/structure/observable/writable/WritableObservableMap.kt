package omnia.data.structure.observable.writable

import omnia.data.contract.Writable
import omnia.data.structure.Map
import omnia.data.structure.mutable.MutableMap
import omnia.data.structure.observable.ObservableMap

interface WritableObservableMap<K : Any, V : Any>
  : MutableMap<K, V>, ObservableMap<K, V>, Writable<ObservableMap<K, V>> {

  companion object {

    fun <K : Any, V : Any> create(): WritableObservableMap<K, V> {
      return WritableObservableMapImpl()
    }

    fun <K : Any, V : Any> copyOf(other: Map<out K, out V>): WritableObservableMap<K, V> {
      return WritableObservableMapImpl(other)
    }
  }
}