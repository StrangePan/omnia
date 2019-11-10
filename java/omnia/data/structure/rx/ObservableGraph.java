package omnia.data.structure.rx;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.data.structure.Graph;
import omnia.data.structure.HomogeneousPair;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.MutableGraph;

public interface ObservableGraph<E> extends MutableGraph<E>, ObservableDataStructure {

  interface GraphMutations<E> {
    Set<? extends GraphMutation<E>> asSet();
  }

  @SuppressWarnings("unused")
  interface GraphMutation<E> {

    static <E> Function<GraphMutation<E>, Flowable<AddNodeToGraph<E>>>
        justAddNodeToGraphMutations() {
      return mutation -> mutation instanceof AddNodeToGraph<?>
          ? Flowable.just((AddNodeToGraph<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<GraphMutation<E>, Flowable<RemoveNodeFromGraph<E>>>
        justRemoveNodeFromGraphMutations() {
      return mutation -> mutation instanceof RemoveNodeFromGraph<?>
          ? Flowable.just((RemoveNodeFromGraph<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<GraphMutation<E>, Flowable<AddEdgeToGraph<E>>>
        justAddEdgeToGraphMutations() {
      return mutation -> mutation instanceof AddEdgeToGraph<?>
          ? Flowable.just((AddEdgeToGraph<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<GraphMutation<E>, Flowable<RemoveEdgeFromGraph<E>>>
        justRemoveEdgeFromGraphMutations() {
      return mutation -> mutation instanceof ObservableGraph.RemoveEdgeFromGraph<?>
          ? Flowable.just((RemoveEdgeFromGraph<E>) mutation)
          : Flowable.empty();
    }
  }

  interface GraphNodeMutation<E> extends GraphMutation<E> {
    E item();
  }

  interface GraphEdgeMutation<E> extends GraphMutation<E> {
    HomogeneousPair<E> endpoints();
  }

  interface AddNodeToGraph<E> extends GraphNodeMutation<E> {}

  interface RemoveNodeFromGraph<E> extends GraphNodeMutation<E> {}

  interface AddEdgeToGraph<E> extends GraphEdgeMutation<E> {}

  interface RemoveEdgeFromGraph<E> extends GraphEdgeMutation<E> {}

  @Override
  ObservableChannels<? extends Graph<E>, ? extends GraphMutations<E>> observe();
}
