package omnia.data.structure.observable;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.data.structure.Set;

public interface ObservableSet<E> extends ObservableDataStructure, Set<E> {

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableDataStructure.ObservableChannels {

    @Override
    Flowable<? extends Set<E>> states();

    @Override
    Flowable<? extends ObservableSet.MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableDataStructure.MutationEvent {

    @Override
    Set<E> state();

    @Override
    Set<? extends ObservableSet.SetOperation<E>> operations();
  }

  interface SetOperation<E> {

    E item();

    static <E> Flowable<AddToSet<E>> justAddToSetOperations(
        Flowable<? extends SetOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof AddToSet<?>
              ? Flowable.just((AddToSet<E>) mutation)
              : Flowable.empty());
    }

    static <E> Flowable<RemoveFromSet<E>> justRemoveFromSetOperations(
        Flowable<? extends SetOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof RemoveFromSet<?>
              ? Flowable.just((RemoveFromSet<E>) mutation)
              : Flowable.empty());
    }
  }

  interface AddToSet<E> extends SetOperation<E> {}

  interface RemoveFromSet<E> extends SetOperation<E> {}
}
