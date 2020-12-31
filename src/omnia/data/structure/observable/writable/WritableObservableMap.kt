package omnia.data.structure.observable.writable

import omnia.data.contract.Writable
import omnia.data.structure.Map
import omnia.data.structure.mutable.MutableMap
import omnia.data.structure.observable.ObservableMap

interface WritableObservableMap<K, V> : MutableMap<K, V>, ObservableMap<K, V>,
  Writable<ObservableMap<K, V>> {

  companion object {

    fun <K, V> create(): WritableObservableMap<K, V> {
      return WritableObservableMapImpl()
    }

    fun <K, V> copyOf(other: Map<out K, out V>): WritableObservableMap<K, V> {
      return WritableObservableMapImpl(other)
    }
  }
}