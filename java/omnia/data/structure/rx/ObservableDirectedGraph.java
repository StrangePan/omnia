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
    Set<? extends GraphOperation<E>> operations();
  }

  interface GraphOperation<E> extends ObservableGraph.GraphOperation<E> {}

  interface NodeOperation<E> extends ObservableGraph.NodeOperation<E>, GraphOperation<E> {}

  interface EdgeOperation<E> extends ObservableGraph.EdgeOperation<E>, GraphOperation<E> {

    E start();

    E end();
  }

  interface AddNodeToGraph<E> extends ObservableGraph.AddNodeToGraph<E>, NodeOperation<E> {}

  interface RemoveNodeFromGraph<E> extends ObservableGraph.RemoveNodeFromGraph<E>, NodeOperation<E> {}

  interface AddEdgeToGraph<E> extends ObservableGraph.AddEdgeToGraph<E>, EdgeOperation<E> {}

  interface RemoveEdgeFromGraph<E> extends ObservableGraph.RemoveNodeFromGraph<E>, EdgeOperation<E> {}
}
