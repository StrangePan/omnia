package omnia.data.structure.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapNotNull
import omnia.data.structure.Map
import omnia.data.structure.Set

interface ObservableMap<K : Any, V : Any> : Map<K, V>, ObservableDataStructure {

  override fun observe(): ObservableChannels<K, V>

  interface ObservableChannels<K : Any, V : Any> : ObservableDataStructure.ObservableChannels {

    override fun states(): Observable<Map<K, V>>

    override fun mutations(): Observable<MutationEvent<K, V>>
  }

  interface MutationEvent<K : Any, V : Any> : ObservableDataStructure.MutationEvent {

    override fun state(): Map<K, V>

    override fun operations(): Set<out MapOperation<K, V>>
  }

  /**
   * Represents a mutation to a map data structure. Can be cast to a specific subtype using one of
   * the available static functions.
   */
  interface MapOperation<K : Any, V : Any> {

    /** The key associated with the operation.  */
    fun key(): K

    companion object {

      fun <K : Any, V : Any> justAddToMapOperations(operations: Observable<MapOperation<K, V>>):
          Observable<AddToMap<K, V>> {
        return operations.mapNotNull { it as AddToMap<K, V> }
      }

      fun <K : Any, V : Any> justRemoveFromMapOperations(operations: Observable<MapOperation<K, V>>):
          Observable<RemoveFromMap<K, V>> {
        return operations.mapNotNull { it as RemoveFromMap<K, V> }
      }

      fun <K : Any, V : Any> justReplaceInMapOperations(operations: Observable<MapOperation<K, V>>):
          Observable<ReplaceInMap<K, V>> {
        return operations.mapNotNull { it as ReplaceInMap<K, V> }
      }
    }
  }

  /** Represents one entry being added to the map. Contains the corresponding key and value.  */
  interface AddToMap<K : Any, V : Any> : MapOperation<K, V> {

    /** The value added to the map.  */
    fun value(): V
  }

  /** Represents one entry being removed from the map. Contains the corresponding key and value.  */
  interface RemoveFromMap<K : Any, V : Any> : MapOperation<K, V> {

    /** The value removed from the map.  */
    fun value(): V
  }

  /**
   * Represents one entry in the map whose value is being replaced with another. Contains the
   * corresponding key, the removed value, and the added value.
   */
  interface ReplaceInMap<K : Any, V : Any> : MapOperation<K, V> {

    /** The value removed from the map.  */
    fun replacedValue(): V

    /** The value added to the map.  */
    fun newValue(): V
  }
}