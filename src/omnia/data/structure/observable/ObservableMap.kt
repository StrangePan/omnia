package omnia.data.structure.observable

import io.reactivex.rxjava3.core.Observable
import omnia.data.structure.Map
import omnia.data.structure.Set

interface ObservableMap<K, V> : Map<K, V>, ObservableDataStructure {

  override fun observe(): ObservableChannels<K, V>
  interface ObservableChannels<K, V> : ObservableDataStructure.ObservableChannels {

    override fun states(): Observable<out Map<K, V>>
    override fun mutations(): Observable<out MutationEvent<K, V>>
  }

  interface MutationEvent<K, V> : ObservableDataStructure.MutationEvent {

    override fun state(): Map<K, V>
    override fun operations(): Set<out MapOperation<K, V>>
  }

  /**
   * Represents a mutation to a map data structure. Can be cast to a specific subtype using one of
   * the available static functions.
   */
  interface MapOperation<K, V> {

    /** The key associated with the operation.  */
    fun key(): K

    companion object {

      fun <K, V> justAddToMapOperations(
        observable: Observable<out MapOperation<K, V>>
      ): Observable<AddToMap<K, V>> {
        return observable.flatMap { mutation: MapOperation<K, V> ->
          if (mutation is AddToMap<*, *>) Observable.just(
            mutation as AddToMap<K, V>
          ) else Observable.empty()
        }
      }

      fun <K, V> justRemoveFromMapOperations(
        observable: Observable<out MapOperation<K, V>>
      ): Observable<RemoveFromMap<K, V>> {
        return observable.flatMap { mutation: MapOperation<K, V> ->
          if (mutation is RemoveFromMap<*, *>) Observable.just(
            mutation as RemoveFromMap<K, V>
          ) else Observable.empty()
        }
      }

      fun <K, V> justReplaceInMapOperations(
        observable: Observable<out MapOperation<K, V>>
      ): Observable<ReplaceInMap<K, V>> {
        return observable.flatMap { mutation: MapOperation<K, V> ->
          if (mutation is ReplaceInMap<*, *>) Observable.just(
            mutation as ReplaceInMap<K, V>
          ) else Observable.empty()
        }
      }
    }
  }

  /** Represents one entry being added to the map. Contains the corresponding key and value.  */
  interface AddToMap<K, V> : MapOperation<K, V> {

    /** The value added to the map.  */
    fun value(): V
  }

  /** Represents one entry being removed from the map. Contains the corresponding key and value.  */
  interface RemoveFromMap<K, V> : MapOperation<K, V> {

    /** The value removed from the map.  */
    fun value(): V
  }

  /**
   * Represents one entry in the map whose value is being replaced with another. Contains the
   * corresponding key, the removed value, and the added value.
   */
  interface ReplaceInMap<K, V> : MapOperation<K, V> {

    /** The value removed from the map.  */
    fun replacedValue(): V

    /** The value added to the map.  */
    fun newValue(): V
  }
}