package omnia.data.structure.observable.writable

import com.badoo.reaktive.observable.concatWith
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observable
import com.badoo.reaktive.subject.publish.PublishSubject
import kotlin.concurrent.Volatile
import omnia.data.structure.Collection
import omnia.data.structure.Map
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableMap
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.observable.ObservableMap
import omnia.data.structure.observable.ObservableMap.MapOperation

internal class WritableObservableMapImpl<K : Any, V : Any> : WritableObservableMap<K, V> {

  @Volatile
  private var currentState: ImmutableMap<K, V>
  private val mutationEvents = PublishSubject<MutationEvent>()

  constructor() {
    currentState = ImmutableMap.empty()
  }

  constructor(other: Map<out K, out V>) {
    currentState = ImmutableMap.copyOf(other)
  }

  override fun putMapping(key: K, value: V) {
    mutateState(
      { currentState -> currentState.valueOf(key)?.let { it != value } ?: true },
      { currentState -> currentState.toBuilder().putMapping(key, value).build() }
    ) { previousState, _ ->
      ImmutableSet.of(
        previousState.valueOf(key)
            ?.let { ReplaceInMap(key, it, value) }
            ?: AddToMap(key, value))
    }
  }

  override fun putMappingIfAbsent(key: K, value: () -> V): V {
    mutateState(
      { currentState -> currentState.valueOf(key) == null },
      { currentState -> currentState.toBuilder().putMapping(key, value()).build() }
    ) { _, newState -> ImmutableSet.of(AddToMap(key, newState.valueOf(key)!!)) }
    return currentState.valueOf(key)!!
  }

  override fun removeKey(key: K): V? {
    val removedValue = currentState.valueOf(key)
    mutateState(
      { currentState -> currentState.valueOf(key) != null },
      { currentState -> currentState.toBuilder().removeKey(key).build() }
    ) { previousState, _ ->
      ImmutableSet.of(
        RemoveFromMap(
          key,
          previousState.valueOf(key)!!
        )
      )
    }
    return removedValue
  }

  override fun removeUnknownTypedKey(key: Any?): V? {
    val removedValue = currentState.valueOfUnknownTyped(key)
    mutateState(
      { currentState -> currentState.valueOfUnknownTyped(key) != null },
      { currentState -> currentState.toBuilder().removeUnknownTypedKey(key).build() }
    ) { previousState, _ ->
      @Suppress("UNCHECKED_CAST")
      ImmutableSet.of(
        RemoveFromMap(
          key as K, previousState.valueOfUnknownTyped(key)!!
        )
      )
    }
    return removedValue
  }

  private fun mutateState(
    shouldChange: (ImmutableMap<K, V>) -> Boolean,
    mutateState: (ImmutableMap<K, V>) -> ImmutableMap<K, V>,
    mutationOperations: (ImmutableMap<K, V>, ImmutableMap<K, V>) -> Set<MapOperation<K, V>>
  ): Boolean {
    val previousState = currentState
    if (!shouldChange(previousState)) {
      return false
    }
    val nextState = mutateState(previousState)
    currentState = nextState
    val operations = mutationOperations(previousState, nextState)
    mutationEvents.onNext(MutationEvent(nextState, operations))
    return true
  }

  override val entries: Set<Map.Entry<K, V>>
    get() {
      return state.entries
    }

  override val keys: Set<K>
    get() {
      return state.keys
    }

  override val values: Collection<V>
    get() {
      return state.values
    }

  override fun valueOf(key: K): V? {
    return state.valueOf(key)
  }

  override fun valueOfUnknownTyped(key: Any?): V? {
    return state.valueOfUnknownTyped(key)
  }

  override fun keysOf(value: V): Set<K> {
    return state.keysOf(value)
  }

  override fun keysOfUnknownTyped(value: Any?): Set<K> {
    return state.keysOfUnknownTyped(value)
  }

  private val state: ImmutableMap<K, V> get() = currentState

  override val observables = Observables()

  inner class Observables : GenericObservables<Map<K, V>, MutationEvent>(
    observable<Map<K, V>> {
      it.onNext(state)
      it.onComplete()
    }
      .concatWith(mutationEvents.map { it.state }),
    observable<MutationEvent> {
      it.onNext(generateMutationEventForNewSubscription())
      it.onComplete()
    }
      .concatWith(mutationEvents)),
    ObservableMap.Observables<K, V>

  private fun generateMutationEventForNewSubscription(): MutationEvent {
    val state: Map<K, V> = state
    return MutationEvent(
      state,
      state.entries
        .map { AddToMap(it.key, it.value) }
        .toImmutableSet())
  }

  inner class MutationEvent(state: Map<K, V>, operations: Set<MapOperation<K, V>>) :
    GenericMutationEvent<Map<K, V>, Set<MapOperation<K, V>>>(state, operations),
    ObservableMap.MutationEvent<K, V>

  override fun toReadOnly(): ObservableMap<K, V> {
    return object : ObservableMap<K, V> {
      override val observables: ObservableMap.Observables<K, V>
        get() {
          return this@WritableObservableMapImpl.observables
        }

      override val keys: Set<K>
        get() {
          return this@WritableObservableMapImpl.keys
        }

      override val values: Collection<V>
        get() {
          return this@WritableObservableMapImpl.values
        }

      override val entries: Set<Map.Entry<K, V>>
        get() {
          return this@WritableObservableMapImpl.entries
        }

      override fun valueOfUnknownTyped(key: Any?): V? {
        return this@WritableObservableMapImpl.valueOfUnknownTyped(key)
      }

      override fun keysOfUnknownTyped(value: Any?): Set<K> {
        return this@WritableObservableMapImpl.keysOfUnknownTyped(value)
      }
    }
  }

  private class AddToMap<K : Any, V : Any>(override val key: K, override val value: V) :
    ObservableMap.AddToMap<K, V>

  private class RemoveFromMap<K : Any, V : Any>(override val key: K, override val value: V) :
    ObservableMap.RemoveFromMap<K, V>

  private class ReplaceInMap<K : Any, V : Any>(
    override val key: K,
    override val replacedValue: V,
    override val newValue: V
  ) : ObservableMap.ReplaceInMap<K, V>
}
