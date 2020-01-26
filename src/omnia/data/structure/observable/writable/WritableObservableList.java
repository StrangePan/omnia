package omnia.data.structure.observable.writable;

import omnia.data.contract.Writable;
import omnia.data.structure.mutable.MutableList;
import omnia.data.structure.observable.ObservableList;

public interface WritableObservableList<E>
    extends MutableList<E>, ObservableList<E>, Writable<ObservableList<E>> {

  static <E> WritableObservableList<E> create() {
    return new WritableObservableListImpl<>();
  }
}
