package omnia.data.structure.rx;

import io.reactivex.Flowable;
import omnia.data.structure.Set;
import omnia.data.structure.UndirectedGraph;
import omnia.data.structure.mutable.MutableUndirectedGraph;

public interface ObservableUndirectedGraph<E> extends ObservableGraph<E>, MutableUndirectedGraph<E> {

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
    Set<? extends UndirectedGraphOperation<E>> operations();
  }

  interface UndirectedGraphOperation<E> extends GraphOperation<E> {}

  interface UndirectedGraphNodeOperation<E> extends UndirectedGraphOperation<E>, GraphNodeOperation<E> {}

  interface UndirectedGraphEdgeOperation<E> extends UndirectedGraphOperation<E>, GraphEdgeOperation<E> {}

  interface AddNodeToUndirectedGraph<E> extends UndirectedGraphNodeOperation<E>, AddNodeToGraph<E> {}

  interface RemoveNodeFromUndirectedGraph<E> extends UndirectedGraphNodeOperation<E>, RemoveNodeFromGraph<E> {}

  interface AddEdgeToUndirectedGraph<E> extends UndirectedGraphEdgeOperation<E>, AddEdgeToGraph<E> {}

  interface RemoveEdgeFromUndirectedGraph<E> extends UndirectedGraphEdgeOperation<E>, RemoveEdgeFromGraph<E> {}
}
