package omnia.data.structure.rx;

import io.reactivex.Flowable;
import omnia.data.structure.Set;
import omnia.data.structure.UndirectedGraph;
import omnia.data.structure.UnorderedPair;
import omnia.data.structure.mutable.MutableSet;
import omnia.data.structure.mutable.MutableUndirectedGraph;

public interface ObservableUndirectedGraph<E> extends ObservableGraph<E>, MutableUndirectedGraph<E> {

  interface Node<E> extends MutableUndirectedGraph.Node<E> {

    @Override
    MutableSet<? extends Edge<E>> edges();

    @Override
    MutableSet<? extends Node<E>> neighbors();
  }

  interface Edge<E> extends MutableUndirectedGraph.Edge<E> {

    @Override
    UnorderedPair<? extends Node<E>> endpoints();
  }

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
    Set<? extends GraphOperation<E>> operations();
  }

  interface GraphOperation<E> extends ObservableGraph.GraphOperation<E> {}

  interface NodeOperation<E> extends ObservableGraph.NodeOperation<E>, GraphOperation<E> {}

  interface EdgeOperation<E> extends ObservableGraph.EdgeOperation<E>, GraphOperation<E> {

    @Override
    UnorderedPair<E> endpoints();
  }

  interface AddNodeToGraph<E> extends ObservableGraph.AddNodeToGraph<E>, NodeOperation<E> {}

  interface RemoveNodeFromGraph<E> extends ObservableGraph.RemoveNodeFromGraph<E>, NodeOperation<E> {}

  interface AddEdgeToGraph<E> extends ObservableGraph.AddEdgeToGraph<E>, EdgeOperation<E> {}

  interface RemoveEdgeFromGraph<E> extends ObservableGraph.RemoveEdgeFromGraph<E>, EdgeOperation<E> {}
}
