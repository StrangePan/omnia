package omnia.data.structure.observable;

import io.reactivex.rxjava3.core.Observable;
import omnia.data.structure.Graph;
import omnia.data.structure.Set;
import omnia.data.structure.tuple.Couplet;

public interface ObservableGraph<E> extends Graph<E>, ObservableDataStructure {

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableDataStructure.ObservableChannels {

    @Override
    Observable<? extends Graph<E>> states();

    @Override
    Observable<? extends ObservableGraph.MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableDataStructure.MutationEvent {

    @Override
    Graph<E> state();

    @Override
    Set<? extends ObservableGraph.GraphOperation<E>> operations();
  }

  interface GraphOperation<E> {

    static <E> Observable<AddNodeToGraph<E>> justAddNodeToGraphOperations(
        Observable<? extends GraphOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof AddNodeToGraph<?>
              ? Observable.just((AddNodeToGraph<E>) mutation)
              : Observable.empty());
    }

    static <E> Observable<RemoveNodeFromGraph<E>> justRemoveNodeFromGraphOperations(
        Observable<? extends GraphOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof RemoveNodeFromGraph<?>
              ? Observable.just((RemoveNodeFromGraph<E>) mutation)
              : Observable.empty());
    }

    static <E> Observable<AddEdgeToGraph<E>> justAddEdgeToGraphOperations(
        Observable<? extends GraphOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof AddEdgeToGraph<?>
              ? Observable.just((AddEdgeToGraph<E>) mutation)
              : Observable.empty());
    }

    static <E> Observable<RemoveEdgeFromGraph<E>> justRemoveEdgeFromGraphOperations(
        Observable<? extends GraphOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof RemoveEdgeFromGraph<?>
              ? Observable.just((RemoveEdgeFromGraph<E>) mutation)
              : Observable.empty());
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
