package omnia.data.structure.observable;

import io.reactivex.rxjava3.core.Observable;
import omnia.data.structure.Set;
import omnia.data.structure.UndirectedGraph;

public interface ObservableUndirectedGraph<E> extends ObservableGraph<E>, UndirectedGraph<E> {

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableGraph.ObservableChannels<E> {

    @Override
    Observable<? extends UndirectedGraph<E>> states();

    @Override
    Observable<? extends MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableGraph.MutationEvent<E> {

    @Override
    UndirectedGraph<E> state();

    @Override
    Set<? extends ObservableGraph.GraphOperation<E>> operations();
  }
}
