package omnia.data.structure.rx;

import omnia.data.structure.mutable.MutableUndirectedGraph;

public interface WritableObservableUndirectedGraph<E>
    extends MutableUndirectedGraph<E>,
    ObservableUndirectedGraph<E>,
    WritableObservableGraph<E> {

  @Override
  ObservableUndirectedGraph<E> toReadOnly();
}
