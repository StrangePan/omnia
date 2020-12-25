package omnia.data.structure.observable;

import io.reactivex.rxjava3.core.Observable;
import omnia.data.structure.Map;
import omnia.data.structure.Set;

public interface ObservableMap<K, V> extends Map<K, V>, ObservableDataStructure {

  @Override
  ObservableChannels<K, V> observe();

  interface ObservableChannels<K, V> extends ObservableDataStructure.ObservableChannels {

    @Override
    Observable<? extends Map<K, V>> states();

    @Override
    Observable<? extends ObservableMap.MutationEvent<K, V>> mutations();
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

    static <K, V> Observable<AddToMap<K, V>> justAddToMapOperations(
        Observable<? extends MapOperation<K, V>> observable) {
      return observable.flatMap(
          mutation -> mutation instanceof AddToMap<?, ?>
              ? Observable.just((AddToMap<K, V>) mutation)
              : Observable.empty());
    }

    static <K, V> Observable<RemoveFromMap<K, V>> justRemoveFromMapOperations(
        Observable<? extends MapOperation<K, V>> observable) {
      return observable.flatMap(
          mutation -> mutation instanceof RemoveFromMap<?, ?>
              ? Observable.just((RemoveFromMap<K, V>) mutation)
              : Observable.empty());
    }

    static <K, V> Observable<ReplaceInMap<K, V>> justReplaceInMapOperations(
        Observable<? extends MapOperation<K, V>> observable) {
      return observable.flatMap(
          mutation -> mutation instanceof ReplaceInMap<?, ?>
              ? Observable.just((ReplaceInMap<K, V>) mutation)
              : Observable.empty());
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
