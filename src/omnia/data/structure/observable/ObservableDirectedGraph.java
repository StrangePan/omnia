package omnia.data.structure.observable;

import io.reactivex.rxjava3.core.Observable;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Set;

public interface ObservableDirectedGraph<E> extends DirectedGraph<E>, ObservableGraph<E> {
  
  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableGraph.ObservableChannels<E> {

    @Override
    Observable<? extends DirectedGraph<E>> states();

    @Override
    Observable<? extends MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableGraph.MutationEvent<E> {

    @Override
    DirectedGraph<E> state();

    @Override
    Set<? extends ObservableGraph.GraphOperation<E>> operations();
  }
}
