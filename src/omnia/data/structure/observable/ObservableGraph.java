package omnia.data.structure.observable;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.data.structure.Graph;
import omnia.data.structure.Set;
import omnia.data.structure.tuple.Couplet;

public interface ObservableGraph<E> extends Graph<E>, ObservableDataStructure {

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableDataStructure.ObservableChannels {

    @Override
    Flowable<? extends Graph<E>> states();

    @Override
    Flowable<? extends ObservableGraph.MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableDataStructure.MutationEvent {

    @Override
    Graph<E> state();

    @Override
    Set<? extends ObservableGraph.GraphOperation<E>> operations();
  }

  interface GraphOperation<E> {

    static <E> Flowable<AddNodeToGraph<E>> justAddNodeToGraphOperations(
        Flowable<? extends GraphOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof AddNodeToGraph<?>
              ? Flowable.just((AddNodeToGraph<E>) mutation)
              : Flowable.empty());
    }

    static <E> Flowable<RemoveNodeFromGraph<E>> justRemoveNodeFromGraphOperations(
        Flowable<? extends GraphOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof RemoveNodeFromGraph<?>
              ? Flowable.just((RemoveNodeFromGraph<E>) mutation)
              : Flowable.empty());
    }

    static <E> Flowable<AddEdgeToGraph<E>> justAddEdgeToGraphOperations(
        Flowable<? extends GraphOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof AddEdgeToGraph<?>
              ? Flowable.just((AddEdgeToGraph<E>) mutation)
              : Flowable.empty());
    }

    static <E> Flowable<RemoveEdgeFromGraph<E>> justRemoveEdgeFromGraphOperations(
        Flowable<? extends GraphOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof RemoveEdgeFromGraph<?>
              ? Flowable.just((RemoveEdgeFromGraph<E>) mutation)
              : Flowable.empty());
    }
  }

  interface NodeOperation<E> extends GraphOperation<E> {
    E item();
  }

  interface EdgeOperation<E> extends GraphOperation<E> {
    Couplet<E> endpoints();
  }

  interface AddNodeToGraph<E> extends NodeOperation<E> {}

  interface RemoveNodeFromGraph<E> extends NodeOperation<E> {}

  interface AddEdgeToGraph<E> extends EdgeOperation<E> {}

  interface RemoveEdgeFromGraph<E> extends EdgeOperation<E> {}
}
