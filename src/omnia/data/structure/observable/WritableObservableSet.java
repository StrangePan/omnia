package omnia.data.structure.observable;

import omnia.data.contract.Writable;
import omnia.data.structure.mutable.MutableSet;

public interface WritableObservableSet<E> extends MutableSet<E>, ObservableSet<E>, Writable<ObservableSet<E>> {

  static <E> WritableObservableSet<E> create() {
    return new WritableObservableSetImpl<>();
  }
}
