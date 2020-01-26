package omnia.data.structure.observable;

import omnia.data.contract.Writable;
import omnia.data.structure.mutable.MutableGraph;

public interface WritableObservableGraph<E>
    extends MutableGraph<E>, ObservableGraph<E>, Writable<ObservableGraph<E>> {}
