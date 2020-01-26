package omnia.data.structure.observable.writable;

import omnia.data.structure.mutable.MutableDirectedGraph;
import omnia.data.structure.observable.ObservableDirectedGraph;

public interface WritableObservableDirectedGraph<E>
    extends MutableDirectedGraph<E>, ObservableDirectedGraph<E>, WritableObservableGraph<E> {

  static <E> WritableObservableDirectedGraph<E> create() {
    return new WritableObservableDirectedGraphImpl<>();
  }

  @Override
  ObservableDirectedGraph<E> toReadOnly();
}
