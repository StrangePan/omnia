package omnia.data.structure.rx;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.MutableSet;

public interface ObservableSet<E> extends MutableSet<E>, ObservableDataStructure {

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableDataStructure.ObservableChannels {

    @Override
    Flowable<? extends Set<E>> states();

    @Override
    Flowable<? extends MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableDataStructure.MutationEvent {

    @Override
    Set<E> state();

    @Override
    Set<? extends SetOperation<E>> operations();
  }

  @SuppressWarnings("unused")
  interface SetOperation<E> {

    static <E> Function<SetOperation<E>, Flowable<AddToSet<E>>> justAddToSetOperations() {
      return mutation -> mutation instanceof AddToSet<?>
          ? Flowable.just((AddToSet<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<SetOperation<E>, Flowable<RemoveFromSet<E>>> justRemoveFromSetOperations() {
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
