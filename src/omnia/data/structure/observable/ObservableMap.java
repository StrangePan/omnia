package omnia.data.structure.observable;

import io.reactivex.Flowable;
import omnia.data.structure.Map;
import omnia.data.structure.Set;

public interface ObservableMap<K, V> extends Map<K, V>, ObservableDataStructure {

  @Override
  ObservableChannels<K, V> observe();

  interface ObservableChannels<K, V> extends ObservableDataStructure.ObservableChannels {

    @Override
    Flowable<? extends Map<K, V>> states();

    @Override
    Flowable<? extends ObservableMap.MutationEvent<K, V>> mutations();
  }

  interface MutationEvent<K, V> extends ObservableDataStructure.MutationEvent {

    @Override
    Map<K, V> state();

    @Override
    Set<? extends MapOperation<K, V>> operations();
  }

  /**
   * Represents a mutation to a map data structure. Can be cast to a specific subtype using one of
   * the available static functions.
   */
  interface MapOperation<K, V> {

    /** The key associated with the operation. */
    K key();

    static <K, V> Flowable<AddToMap<K, V>> justAddToMapOperations(
        Flowable<? extends MapOperation<K, V>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof AddToMap<?, ?>
              ? Flowable.just((AddToMap<K, V>) mutation)
              : Flowable.empty());
    }

    static <K, V> Flowable<RemoveFromMap<K, V>> justRemoveFromMapOperations(
        Flowable<? extends MapOperation<K, V>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof RemoveFromMap<?, ?>
              ? Flowable.just((RemoveFromMap<K, V>) mutation)
              : Flowable.empty());
    }

    static <K, V> Flowable<ReplaceInMap<K, V>> justReplaceInMapOperations(
        Flowable<? extends MapOperation<K, V>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof ReplaceInMap<?, ?>
              ? Flowable.just((ReplaceInMap<K, V>) mutation)
              : Flowable.empty());
    }
  }

  /** Represents one entry being added to the map. Contains the corresponding key and value. */
  interface AddToMap<K, V> extends MapOperation<K, V> {

    /** The value added to the map. */
    V value();
  }

  /** Represents one entry being removed from the map. Contains the corresponding key and value. */
  interface RemoveFromMap<K, V> extends MapOperation<K, V> {

    /** The value removed from the map. */
    V value();
  }

  /**
   * Represents one entry in the map whose value is being replaced with another. Contains the
   * corresponding key, the removed value, and the added value.
   */
  interface ReplaceInMap<K, V> extends MapOperation<K, V> {

    /** The value removed from the map. */
    V replacedValue();

    /** The value added to the map. */
    V newValue();
  }
}
