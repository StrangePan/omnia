package omnia.data.structure.rx;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.MutableSet;

public interface ObservableSet<E> extends MutableSet<E>, ObservableDataStructure {

  interface SetMutations<E> {
    Set<? extends SetMutation<E>> asSet();
  }

  @SuppressWarnings("unused")
  interface SetMutation<E> {

    static <E> Function<SetMutation<E>, Flowable<AddToSet<E>>> justAddToSetMutations() {
      return mutation -> mutation instanceof AddToSet<?>
          ? Flowable.just((AddToSet<E>) mutation)
          : Flowable.empty();
    }

    static <E> Function<SetMutation<E>, Flowable<RemoveFromSet<E>>> justRemoveFromSetMutations() {
      return mutation -> mutation instanceof RemoveFromSet<?>
          ? Flowable.just((RemoveFromSet<E>) mutation)
          : Flowable.empty();
    }
  }

  interface AddToSet<E> extends SetMutation<E> {
    E item();
  }

  interface RemoveFromSet<E> extends SetMutation<E> {
    E item();
  }

  @Override
  ObservableChannels<? extends Set<E>, ? extends SetMutations<E>> observe();
}
