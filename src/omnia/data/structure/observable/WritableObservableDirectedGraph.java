package omnia.data.structure.observable;

import omnia.data.structure.mutable.MutableDirectedGraph;

public interface WritableObservableDirectedGraph<E>
    extends MutableDirectedGraph<E>, ObservableDirectedGraph<E>, WritableObservableGraph<E> {

  static <E> WritableObservableDirectedGraph<E> create() {
    return new WritableObservableDirectedGraphImpl<>();
  }

  @Override
  ObservableDirectedGraph<E> toReadOnly();
}
