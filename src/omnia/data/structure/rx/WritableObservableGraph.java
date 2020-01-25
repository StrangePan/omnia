package omnia.data.structure.rx;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.data.structure.Graph;
import omnia.data.structure.HomogeneousPair;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.MutableGraph;

public interface WritableObservableGraph<E> extends MutableGraph<E>, ObservableDataStructure {

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
      return mutation -> mutation instanceof WritableObservableGraph.AddNodeToGraph<?>
          ? Flowable.just((AddNodeToGraph<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<GraphOperation<E>, Flowable<RemoveNodeFromGraph<E>>>
        justRemoveNodeFromGraphMutations() {
      return mutation -> mutation instanceof WritableObservableGraph.RemoveNodeFromGraph<?>
          ? Flowable.just((RemoveNodeFromGraph<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<GraphOperation<E>, Flowable<AddEdgeToGraph<E>>>
        justAddEdgeToGraphMutations() {
      return mutation -> mutation instanceof WritableObservableGraph.AddEdgeToGraph<?>
          ? Flowable.just((AddEdgeToGraph<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<GraphOperation<E>, Flowable<RemoveEdgeFromGraph<E>>>
        justRemoveEdgeFromGraphMutations() {
      return mutation -> mutation instanceof WritableObservableGraph.RemoveEdgeFromGraph<?>
          ? Flowable.just((RemoveEdgeFromGraph<E>) mutation)
          : Flowable.empty();
    }
  }

  interface NodeOperation<E> extends GraphOperation<E> {
    E item();
  }

  interface EdgeOperation<E> extends GraphOperation<E> {
    HomogeneousPair<E> endpoints();
  }

  interface AddNodeToGraph<E> extends NodeOperation<E> {}

  interface RemoveNodeFromGraph<E> extends NodeOperation<E> {}

  interface AddEdgeToGraph<E> extends EdgeOperation<E> {}

  interface RemoveEdgeFromGraph<E> extends EdgeOperation<E> {}
}
