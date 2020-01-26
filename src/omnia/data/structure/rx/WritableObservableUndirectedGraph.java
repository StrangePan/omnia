package omnia.data.structure.rx;

import omnia.data.contract.Writable;
import omnia.data.structure.mutable.MutableUndirectedGraph;

public interface WritableObservableUndirectedGraph<E>
    extends MutableUndirectedGraph<E>,
    ObservableUndirectedGraph<E>,
    Writable<ObservableUndirectedGraph<E>>,
    WritableObservableGraph<E> {}
