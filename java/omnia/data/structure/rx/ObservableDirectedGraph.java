package omnia.data.structure.rx;

import io.reactivex.Flowable;
import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.MutableDirectedGraph;

public interface ObservableDirectedGraph<E> extends MutableDirectedGraph<E>, ObservableGraph<E> {

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
    Set<? extends DirectedGraphOperation<E>> operations();
  }

  interface DirectedGraphOperation<E> extends GraphOperation<E> {}

  interface DirectedGraphNodeOperation<E> extends GraphNodeOperation<E> {}

  interface DirectedGraphEdgeOperation<E> extends GraphEdgeOperation<E> {

    E start();

    E end();
  }

  interface AddNodeToDirectedGraph<E> extends AddNodeToGraph<E>, DirectedGraphNodeOperation<E> {}

  interface RemoveNodeFromDirectedGraph<E> extends RemoveNodeFromGraph<E>, DirectedGraphNodeOperation<E> {}

  interface AddEdgeToDirectedGraph<E> extends AddEdgeToGraph<E>, DirectedGraphEdgeOperation<E> {}

  interface RemoveEdgeFromDirectedGraph<E> extends RemoveNodeFromGraph<E>, DirectedGraphEdgeOperation<E> {}
}
