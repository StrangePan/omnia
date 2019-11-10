package omnia.data.structure.rx;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.data.structure.Graph;
import omnia.data.structure.HomogeneousPair;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.MutableGraph;

public interface ObservableGraph<E> extends MutableGraph<E>, ObservableDataStructure {

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableDataStructure.ObservableChannels {

    @Override
    Flowable<? extends Graph<E>> states();

    @Override
    Flowable<? extends MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableDataStructure.MutationEvent {

    @Override
    Graph<E> state();

    @Override
    Set<? extends GraphOperation<E>> operations();
  }

  @SuppressWarnings("unused")
  interface GraphOperation<E> {

    static <E> Function<GraphOperation<E>, Flowable<AddNodeToGraph<E>>>
        justAddNodeToGraphMutations() {
      return mutation -> mutation instanceof AddNodeToGraph<?>
          ? Flowable.just((AddNodeToGraph<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<GraphOperation<E>, Flowable<RemoveNodeFromGraph<E>>>
        justRemoveNodeFromGraphMutations() {
      return mutation -> mutation instanceof RemoveNodeFromGraph<?>
          ? Flowable.just((RemoveNodeFromGraph<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<GraphOperation<E>, Flowable<AddEdgeToGraph<E>>>
        justAddEdgeToGraphMutations() {
      return mutation -> mutation instanceof AddEdgeToGraph<?>
          ? Flowable.just((AddEdgeToGraph<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<GraphOperation<E>, Flowable<RemoveEdgeFromGraph<E>>>
        justRemoveEdgeFromGraphMutations() {
      return mutation -> mutation instanceof ObservableGraph.RemoveEdgeFromGraph<?>
          ? Flowable.just((RemoveEdgeFromGraph<E>) mutation)
          : Flowable.empty();
    }
  }

  interface GraphNodeOperation<E> extends GraphOperation<E> {
    E item();
  }

  interface GraphEdgeOperation<E> extends GraphOperation<E> {
    HomogeneousPair<E> endpoints();
  }

  interface AddNodeToGraph<E> extends GraphNodeOperation<E> {}

  interface RemoveNodeFromGraph<E> extends GraphNodeOperation<E> {}

  interface AddEdgeToGraph<E> extends GraphEdgeOperation<E> {}

  interface RemoveEdgeFromGraph<E> extends GraphEdgeOperation<E> {}
}
