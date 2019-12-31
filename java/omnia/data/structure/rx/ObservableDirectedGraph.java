package omnia.data.structure.rx;

import io.reactivex.Flowable;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.MutableDirectedGraph;

public interface ObservableDirectedGraph<E> extends MutableDirectedGraph<E>, ObservableGraph<E> {

  static <E> ObservableDirectedGraph<E> create() {
    return new ObservableDirectedGraphImpl<>();
  }

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableGraph.ObservableChannels<E> {

    @Override
    Flowable<? extends DirectedGraph<E>> states();

    @Override
    Flowable<? extends MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableGraph.MutationEvent<E> {

    @Override
    DirectedGraph<E> state();

    @Override
    Set<? extends GraphOperation<E>> operations();
  }
}
