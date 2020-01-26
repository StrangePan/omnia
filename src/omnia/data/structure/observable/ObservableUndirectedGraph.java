package omnia.data.structure.observable;

import io.reactivex.Flowable;
import omnia.data.structure.Set;
import omnia.data.structure.UndirectedGraph;

public interface ObservableUndirectedGraph<E> extends ObservableGraph<E>, UndirectedGraph<E> {

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableGraph.ObservableChannels<E> {

    @Override
    Flowable<? extends UndirectedGraph<E>> states();

    @Override
    Flowable<? extends MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableGraph.MutationEvent<E> {

    @Override
    UndirectedGraph<E> state();

    @Override
    Set<? extends ObservableGraph.GraphOperation<E>> operations();
  }
}
