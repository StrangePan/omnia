package omnia.data.structure.observable.writable;

import omnia.data.structure.mutable.MutableUndirectedGraph;
import omnia.data.structure.observable.ObservableUndirectedGraph;

public interface WritableObservableUndirectedGraph<E>
    extends MutableUndirectedGraph<E>,
    ObservableUndirectedGraph<E>,
    WritableObservableGraph<E> {

  @Override
  ObservableUndirectedGraph<E> toReadOnly();
}
