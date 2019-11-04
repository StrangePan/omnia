package omnia.data.structure.rx;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.contract.Countable;
import omnia.contract.Streamable;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.MutableSet;

public interface ObservableSet<E> extends MutableSet<E>, ObservableDataStructure<Set<E>, ObservableSet.SetMutations<E>> {

  interface SetMutations<E> extends Iterable<SetMutation<E>>, Streamable<SetMutation<E>>, Countable {
    Set<SetMutation<E>> asSet();
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
}
