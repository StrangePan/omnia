package omnia.data.structure.observable.writable;

import omnia.data.contract.Writable;
import omnia.data.structure.mutable.MutableMap;
import omnia.data.structure.observable.ObservableMap;

public interface WritableObservableMap<K, V>
    extends MutableMap<K, V>, ObservableMap<K, V>, Writable<ObservableMap<K, V>> {

  static <K, V> WritableObservableMap<K, V> create() {
    return new WritableObservableMapImpl<>();
  }
}
