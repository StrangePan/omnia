package omnia.data.structure.rx;

import io.reactivex.Flowable;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.MutableDirectedGraph;

public interface WritableObservableDirectedGraph<E> extends MutableDirectedGraph<E>, WritableObservableGraph<E> {

  static <E> WritableObservableDirectedGraph<E> create() {
    return new WritableObservableDirectedGraphImpl<>();
  }

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends WritableObservableGraph.ObservableChannels<E> {

    @Override
    Flowable<? extends DirectedGraph<E>> states();

    @Override
    Flowable<? extends MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends WritableObservableGraph.MutationEvent<E> {

    @Override
    DirectedGraph<E> state();

    @Override
    Set<? extends GraphOperation<E>> operations();
  }
}
