package omnia.data.structure.observable;

import io.reactivex.rxjava3.core.Observable;
import omnia.data.structure.Set;

public interface ObservableSet<E> extends ObservableDataStructure, Set<E> {

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableDataStructure.ObservableChannels {

    @Override
    Observable<? extends Set<E>> states();

    @Override
    Observable<? extends ObservableSet.MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableDataStructure.MutationEvent {

    @Override
    Set<E> state();

    @Override
    Set<? extends ObservableSet.SetOperation<E>> operations();
  }

  interface SetOperation<E> {

    E item();

    static <E> Observable<AddToSet<E>> justAddToSetOperations(
        Observable<? extends SetOperation<E>> observable) {
      return observable.flatMap(
          mutation -> mutation instanceof AddToSet<?>
              ? Observable.just((AddToSet<E>) mutation)
              : Observable.empty());
    }

    static <E> Observable<RemoveFromSet<E>> justRemoveFromSetOperations(
        Observable<? extends SetOperation<E>> observable) {
      return observable.flatMap(
          mutation -> mutation instanceof RemoveFromSet<?>
              ? Observable.just((RemoveFromSet<E>) mutation)
              : Observable.empty());
    }
  }

  interface AddToSet<E> extends SetOperation<E> {}

  interface RemoveFromSet<E> extends SetOperation<E> {}
}
