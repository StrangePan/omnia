package omnia.data.structure.rx;

import omnia.data.structure.DirectedGraph;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.MutableDirectedGraph;

public interface ObservableDirectedGraph<E> extends MutableDirectedGraph<E>, ObservableGraph<E> {

  interface DirectedGraphMutations<E> extends GraphMutations<E> {

    @Override
    Set<? extends DirectedGraphMutation<E>> asSet();
  }

  interface DirectedGraphMutation<E> extends GraphMutation<E> {}

  interface DirectedGraphNodeMutation<E> extends GraphNodeMutation<E> {}

  interface DirectedGraphEdgeMutation<E> extends GraphEdgeMutation<E> {

    E start();

    E end();
  }

  interface AddNodeToDirectedGraph<E> extends AddNodeToGraph<E>, DirectedGraphNodeMutation<E> {}

  interface RemoveNodeFromDirectedGraph<E> extends RemoveNodeFromGraph<E>, DirectedGraphNodeMutation<E> {}

  interface AddEdgeToDirectedGraph<E> extends AddEdgeToGraph<E>, DirectedGraphEdgeMutation<E> {}

  interface RemoveEdgeFromDirectedGraph<E> extends RemoveNodeFromGraph<E>, DirectedGraphEdgeMutation<E> {}

  @Override
  ObservableChannels<? extends DirectedGraph<E>, ? extends DirectedGraphMutations<E>> observe();
}
