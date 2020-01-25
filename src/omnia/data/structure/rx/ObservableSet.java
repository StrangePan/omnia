package omnia.data.structure.rx;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.data.structure.Set;

public interface ObservableSet<E> extends ObservableDataStructure {
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

  @SuppressWarnings("unused")
  interface SetOperation<E> {

    static <E> Function<ObservableSet.SetOperation<E>, Flowable<ObservableSet.AddToSet<E>>>
        justAddToSetOperations() {
      return mutation -> mutation instanceof AddToSet<?>
          ? Flowable.just((AddToSet<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<ObservableSet.SetOperation<E>, Flowable<ObservableSet.RemoveFromSet<E>>>
        justRemoveFromSetOperations() {
      return mutation -> mutation instanceof RemoveFromSet<?>
          ? Flowable.just((RemoveFromSet<E>) mutation)
          : Flowable.empty();
    }
  }

  interface AddToSet<E> extends SetOperation<E> {
    E item();
  }

  interface RemoveFromSet<E> extends SetOperation<E> {
    E item();
  }
}
