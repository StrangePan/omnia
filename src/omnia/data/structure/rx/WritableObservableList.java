package omnia.data.structure.rx;

import omnia.data.contract.Writable;
import omnia.data.structure.mutable.MutableList;

public interface WritableObservableList<E>
    extends MutableList<E>, ObservableList<E>, Writable<ObservableList<E>> {

  static <E> WritableObservableList<E> create() {
    return new WritableObservableListImpl<>();
  }
}
