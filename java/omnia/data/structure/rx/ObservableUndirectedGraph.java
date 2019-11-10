package omnia.data.structure.rx;

import omnia.data.structure.Set;
import omnia.data.structure.UndirectedGraph;
import omnia.data.structure.mutable.MutableUndirectedGraph;

public interface ObservableUndirectedGraph<E> extends ObservableGraph<E>, MutableUndirectedGraph<E> {

  interface UndirectedGraphMutations<E> extends GraphMutations<E> {
    @Override
    Set<? extends UndirectedGraphMutation<E>> asSet();
  }

  interface UndirectedGraphMutation<E> extends GraphMutation<E> {}

  interface UndirectedGraphNodeMutation<E> extends UndirectedGraphMutation<E>, GraphNodeMutation<E> {}

  interface UndirectedGraphEdgeMutation<E> extends UndirectedGraphMutation<E>, GraphEdgeMutation<E> {}

  interface AddNodeToUndirectedGraph<E> extends UndirectedGraphNodeMutation<E>, AddNodeToGraph<E> {}

  interface RemoveNodeFromUndirectedGraph<E> extends UndirectedGraphNodeMutation<E>, RemoveNodeFromGraph<E> {}

  interface AddEdgeToUndirectedGraph<E> extends UndirectedGraphEdgeMutation<E>, AddEdgeToGraph<E> {}

  interface RemoveEdgeFromUndirectedGraph<E> extends UndirectedGraphEdgeMutation<E>, RemoveEdgeFromGraph<E> {}

  @Override
  ObservableChannels<? extends UndirectedGraph<E>, ? extends UndirectedGraphMutations<E>> observe();
}
