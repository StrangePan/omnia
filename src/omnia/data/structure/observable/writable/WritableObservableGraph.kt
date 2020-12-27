package omnia.data.structure.observable.writable

import omnia.data.contract.Writable
import omnia.data.structure.mutable.MutableGraph
import omnia.data.structure.observable.ObservableGraph

interface WritableObservableGraph<E> : MutableGraph<E>, ObservableGraph<E>, Writable<ObservableGraph<E>>