package omnia.data.structure.observable.writable;

import omnia.data.structure.DirectedGraph;
import omnia.data.structure.mutable.MutableDirectedGraph;
import omnia.data.structure.observable.ObservableDirectedGraph;

public interface WritableObservableDirectedGraph<E>
    extends MutableDirectedGraph<E>, ObservableDirectedGraph<E>, WritableObservableGraph<E> {

  static <E> WritableObservableDirectedGraph<E> create() {
    return new WritableObservableDirectedGraphImpl<>();
  }

  static <E> WritableObservableDirectedGraph<E> copyOf(DirectedGraph<E> original) {
    return new WritableObservableDirectedGraphImpl<>(original);
  }

  @Override
  ObservableDirectedGraph<E> toReadOnly();
}
