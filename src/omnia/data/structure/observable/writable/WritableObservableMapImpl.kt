package omnia.data.structure.observable.writable

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.Objects
import java.util.function.BiFunction
import java.util.function.Predicate
import java.util.function.Supplier
import omnia.data.stream.Collectors
import omnia.data.structure.Collection
import omnia.data.structure.Map
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableMap
import omnia.data.structure.immutable.ImmutableSet
import omnia.data.structure.observable.ObservableMap
import omnia.data.structure.observable.ObservableMap.MapOperation

internal class WritableObservableMapImpl<K : Any, V : Any> : WritableObservableMap<K, V> {

  @Volatile
  private var currentState: ImmutableMap<K, V>
  private val mutationEvents: Subject<MutationEvent> =
    PublishSubject.create<MutationEvent>().toSerialized()

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

  override fun putMappingIfAbsent(key: K, value: Supplier<V>): V {
    synchronized(this) {
      mutateState(
        { currentState -> currentState.valueOf(key) == null },
        { currentState -> currentState.toBuilder().putMapping(key, value.get()).build() }
      ) { _, newState -> ImmutableSet.of(AddToMap(key, newState.valueOf(key)!!)) }
      return currentState.valueOf(key)!!
    }
  }

  override fun removeKey(key: K): V? {
    synchronized(this) {
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
  }

  override fun removeUnknownTypedKey(key: Any?): V? {
    synchronized(this) {
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
  }

  private fun mutateState(
    shouldChange: Predicate<in ImmutableMap<K, V>>,
    mutateState: java.util.function.Function<in ImmutableMap<K, V>, out ImmutableMap<K, V>>,
    mutationOperations: BiFunction<in ImmutableMap<K, V>, in ImmutableMap<K, V>, out Set<MapOperation<K, V>>>
  ): Boolean {
    synchronized(this) {
      val previousState = currentState
      if (!shouldChange.test(previousState)) {
        return false
      }
      val nextState = Objects.requireNonNull(mutateState.apply(previousState))
      currentState = nextState
      val operations = mutationOperations.apply(previousState, nextState)
      mutationEvents.onNext(MutationEvent(nextState, operations))
      return true
    }
  }

  override fun entries(): Set<Map.Entry<K, V>> {
    return state.entries()
  }

  override fun keys(): Set<K> {
    return state.keys()
  }

  override fun values(): Collection<V> {
    return state.values()
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

  private val state: ImmutableMap<K, V>
    get() {
      synchronized(this) { return currentState }
    }

  override fun observe(): ObservableChannels {
    return ObservableChannels()
  }

  inner class ObservableChannels : GenericObservableChannels<Map<K, V>, MutationEvent>(
    Observable.create { flowableEmitter: ObservableEmitter<Map<K, V>> ->
      flowableEmitter.onNext(state)
      flowableEmitter.onComplete()
    }
      .concatWith(mutationEvents.map { obj: MutationEvent -> obj.state() }),
    Observable.create { flowableEmitter: ObservableEmitter<MutationEvent> ->
      flowableEmitter.onNext(generateMutationEventForNewSubscription())
      flowableEmitter.onComplete()
    }
      .concatWith(mutationEvents)), ObservableMap.ObservableChannels<K, V>

  private fun generateMutationEventForNewSubscription(): MutationEvent {
    val state: Map<K, V> = state
    return MutationEvent(
      state,
      state.entries()
        .stream()
        .map<MapOperation<K, V>> { entry: Map.Entry<K, V> -> AddToMap(entry.key(), entry.value()) }
        .collect(Collectors.toSet()))
  }

  inner class MutationEvent(state: Map<K, V>, operations: Set<MapOperation<K, V>>) :
    GenericMutationEvent<Map<K, V>, Set<MapOperation<K, V>>>(state, operations),
    ObservableMap.MutationEvent<K, V>

  override fun toReadOnly(): ObservableMap<K, V> {
    return object : ObservableMap<K, V> {
      override fun observe(): ObservableMap.ObservableChannels<K, V> {
        return this@WritableObservableMapImpl.observe()
      }

      override fun keys(): Set<K> {
        return this@WritableObservableMapImpl.keys()
      }

      override fun values(): Collection<V> {
        return this@WritableObservableMapImpl.values()
      }

      override fun entries(): Set<Map.Entry<K, V>> {
        return this@WritableObservableMapImpl.entries()
      }

      override fun valueOfUnknownTyped(key: Any?): V? {
        return this@WritableObservableMapImpl.valueOfUnknownTyped(key)
      }

      override fun keysOfUnknownTyped(value: Any?): Set<K> {
        return this@WritableObservableMapImpl.keysOfUnknownTyped(value)
      }
    }
  }

  private class AddToMap<K : Any, V : Any>(private val key: K, private val value: V) :
    ObservableMap.AddToMap<K, V> {

    override fun key(): K {
      return key
    }

    override fun value(): V {
      return value
    }
  }

  private class RemoveFromMap<K : Any, V : Any>(private val key: K, private val value: V) :
    ObservableMap.RemoveFromMap<K, V> {

    override fun key(): K {
      return key
    }

    override fun value(): V {
      return value
    }
  }

  private class ReplaceInMap<K : Any, V : Any>(
    private val key: K,
    private val replacedValue: V,
    private val newValue: V
  ) : ObservableMap.ReplaceInMap<K, V> {

    override fun key(): K {
      return key
    }

    override fun replacedValue(): V {
      return replacedValue
    }

    override fun newValue(): V {
      return newValue
    }
  }
}