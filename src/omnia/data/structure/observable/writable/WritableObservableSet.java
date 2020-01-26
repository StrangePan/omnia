package omnia.data.structure.observable.writable;

import omnia.data.contract.Writable;
import omnia.data.structure.mutable.MutableSet;
import omnia.data.structure.observable.ObservableSet;

public interface WritableObservableSet<E> extends MutableSet<E>, ObservableSet<E>, Writable<ObservableSet<E>> {

  static <E> WritableObservableSet<E> create() {
    return new WritableObservableSetImpl<>();
  }
}
