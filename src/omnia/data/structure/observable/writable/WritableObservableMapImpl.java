package omnia.data.structure.observable.writable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toSet;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import omnia.data.structure.Collection;
import omnia.data.structure.Map;
import omnia.data.structure.Set;
import omnia.data.structure.immutable.ImmutableMap;
import omnia.data.structure.immutable.ImmutableSet;
import omnia.data.structure.observable.ObservableMap;

final class WritableObservableMapImpl<K, V> implements WritableObservableMap<K, V> {

  private volatile ImmutableMap<K, V> currentState;
  private final FlowableProcessor<MutationEvent> mutationEventProcessor =
      PublishProcessor.<MutationEvent>create().toSerialized();

  WritableObservableMapImpl() {
    currentState = ImmutableMap.empty();
  }

  WritableObservableMapImpl(Map<? extends K, ? extends V> other) {
    currentState = ImmutableMap.copyOf(other);
  }

  @Override
  public void putMapping(K key, V value) {
    mutateState(
        currentState -> currentState.valueOf(key).map(v -> !Objects.equals(v, value)).orElse(true),
        currentState -> currentState.toBuilder().putMapping(key, value).build(),
        (previousState, newState) ->
            ImmutableSet.of(
                previousState.valueOf(key)
                    .<MapOperation<K, V>>map(previous -> new ReplaceInMap<>(key, previous, value))
                    .orElseGet(() -> new AddToMap<K, V>(key, value))));
  }

  @Override
  public V putMappingIfAbsent(K key, Supplier<V> value) {
    synchronized (this) {
      mutateState(
          currentState -> currentState.valueOf(key).isEmpty(),
          currentState -> currentState.toBuilder().putMapping(key, value.get()).build(),
          (previousState, newState) ->
              ImmutableSet.of(new AddToMap<>(key, newState.valueOf(key).get())));
      return currentState.valueOf(key).get();
    }
  }

  @Override
  public Optional<V> removeKey(K key) {
    synchronized (this) {
      Optional<V> removedValue = currentState.valueOf(key);
      mutateState(
          currentState -> currentState.valueOf(key).isPresent(),
          currentState -> currentState.toBuilder().removeKey(key).build(),
          (previousState, newState) ->
              ImmutableSet.of(new RemoveFromMap<>(key, previousState.valueOf(key).get())));
      return removedValue;
    }
  }

  @Override
  public Optional<V> removeUnknownTypedKey(Object key) {
    synchronized (this) {
      Optional<V> removedValue = currentState.valueOfUnknownTyped(key);
      mutateState(
          currentState -> currentState.valueOfUnknownTyped(key).isPresent(),
          currentState -> currentState.toBuilder().removeUnknownTypedKey(key).build(),
          (previousState, newState) ->
              ImmutableSet.<MapOperation<K, V>>of(
                  new RemoveFromMap<>((K) key, previousState.valueOfUnknownTyped(key).get())));
      return removedValue;
    }
  }

  private boolean mutateState(
      Predicate<? super ImmutableMap<K, V>> shouldChange,
      Function<? super ImmutableMap<K, V>, ? extends ImmutableMap<K, V>> mutateState,
      BiFunction<? super ImmutableMap<K, V>, ? super ImmutableMap<K, V>, ? extends Set<MapOperation<K, V>>> mutationOperations) {
    synchronized (this) {
      ImmutableMap<K, V> previousState = currentState;
      if (!shouldChange.test(previousState)) {
        return false;
      }
      ImmutableMap<K, V> nextState = requireNonNull(mutateState.apply(previousState));
      currentState = nextState;
      Set<MapOperation<K, V>> operations = mutationOperations.apply(previousState, nextState);
      mutationEventProcessor.onNext(new MutationEvent(nextState, operations));
      return true;
    }
  }

  @Override
  public Set<Entry<K, V>> entries() {
    return getState().entries();
  }

  @Override
  public Set<K> keys() {
    return getState().keys();
  }

  @Override
  public Collection<V> values() {
    return getState().values();
  }

  @Override
  public Optional<V> valueOf(K key) {
    return getState().valueOf(key);
  }

  @Override
  public Optional<V> valueOfUnknownTyped(Object key) {
    return getState().valueOfUnknownTyped(key);
  }

  @Override
  public Set<K> keysOf(V value) {
    return getState().keysOf(value);
  }

  @Override
  public Set<K> keysOfUnknownTyped(Object value) {
    return getState().keysOfUnknownTyped(value);
  }

  private ImmutableMap<K, V> getState() {
    synchronized (this) {
      return currentState;
    }
  }

  @Override
  public ObservableChannels observe() {
    return new ObservableChannels();
  }

  private final class ObservableChannels extends GenericObservableChannels<Map<K, V>, MutationEvent>
      implements ObservableMap.ObservableChannels<K, V> {
    private ObservableChannels() {
      super(
          Flowable.<Map<K, V>>create(
                  flowableEmitter -> {
                    flowableEmitter.onNext(getState());
                    flowableEmitter.onComplete();
                  },
                  BackpressureStrategy.BUFFER)
              .concatWith(mutationEventProcessor.map(MutationEvent::state)),
          Flowable.<MutationEvent>create(
                  flowableEmitter -> {
                    flowableEmitter.onNext(generateMutationEventForNewSubscription());
                    flowableEmitter.onComplete();
                  },
                  BackpressureStrategy.BUFFER)
              .concatWith(mutationEventProcessor));
    }
  }

  private MutationEvent generateMutationEventForNewSubscription() {
    Map<K, V> state = getState();
    return new MutationEvent(
        state,
        state.entries()
            .stream()
            .<MapOperation<K, V>>map(entry -> new AddToMap<>(entry.key(), entry.value()))
            .collect(toSet()));
  }

  private final class MutationEvent extends GenericMutationEvent<Map<K, V>, Set<MapOperation<K, V>>>
      implements ObservableMap.MutationEvent<K, V> {
    private MutationEvent(Map<K, V> state, Set<MapOperation<K, V>> operations) {
      super(state, operations);
    }
  }

  @Override
  public ObservableMap<K, V> toReadOnly() {
    return new ObservableMap<>() {
      @Override
      public ObservableChannels<K, V> observe() {
        return WritableObservableMapImpl.this.observe();
      }

      @Override
      public Set<K> keys() {
        return WritableObservableMapImpl.this.keys();
      }

      @Override
      public Collection<V> values() {
        return WritableObservableMapImpl.this.values();
      }

      @Override
      public Set<Entry<K, V>> entries() {
        return WritableObservableMapImpl.this.entries();
      }

      @Override
      public Optional<V> valueOfUnknownTyped(Object key) {
        return WritableObservableMapImpl.this.valueOfUnknownTyped(key);
      }

      @Override
      public Set<K> keysOfUnknownTyped(Object value) {
        return WritableObservableMapImpl.this.keysOfUnknownTyped(value);
      }
    };
  }

  private static final class AddToMap<K, V> implements ObservableMap.AddToMap<K, V> {
    private final K key;
    private final V value;

    private AddToMap(K key, V value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public K key() {
      return key;
    }

    @Override
    public V value() {
      return value;
    }
  }

  private static final class RemoveFromMap<K, V> implements ObservableMap.RemoveFromMap<K, V> {
    private final K key;
    private final V value;

    private RemoveFromMap(K key, V value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public K key() {
      return key;
    }

    @Override
    public V value() {
      return value;
    }
  }

  private static final class ReplaceInMap<K, V> implements ObservableMap.ReplaceInMap<K, V> {
    private final K key;
    private final V replacedValue;
    private final V newValue;

    private ReplaceInMap(K key, V replacedValue, V newValue) {
      this.key = key;
      this.replacedValue = replacedValue;
      this.newValue = newValue;
    }

    @Override
    public K key() {
      return key;
    }

    @Override
    public V replacedValue() {
      return replacedValue;
    }

    @Override
    public V newValue() {
      return newValue;
    }
  }
}
