package omnia.data.structure.rx;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.contract.Countable;
import omnia.data.structure.List;
public interface ObservableList<E> extends ObservableDataStructure {
  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E2> extends ObservableDataStructure.ObservableChannels {

    @Override
    Flowable<? extends List<E2>> states();

    @Override
    Flowable<? extends WritableObservableList.MutationEvent<E2>> mutations();
  }

  interface MutationEvent<E2> extends ObservableDataStructure.MutationEvent {

    @Override
    List<E2> state();

    @Override
    List<? extends ObservableList.ListOperation<E2>> operations();
  }

  /**
   * Represents a mutation to a list data structure. Can be cast to a specific subtype using one
   * of the available static functions.
   */
  @SuppressWarnings("unused") // E unused, but required for type safety when upcasting
  interface ListOperation<E2> {

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListOperation} stream to only the {@link AddToList} operations.
     */
    static <E> Function<ObservableList.ListOperation<E>, Flowable<ObservableList.AddToList<E>>> justAddToListMutations() {
      return mutation -> mutation instanceof AddToList
          ? Flowable.just((AddToList) mutation)
          : Flowable.empty();
    }

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListOperation} stream to only the {@link MoveInList} operations.
     */
    static <E> Function<ObservableList.ListOperation<E>, Flowable<ObservableList.MoveInList<E>>> justMoveInListMutations() {
      return mutation -> mutation instanceof MoveInList
          ? Flowable.just((MoveInList) mutation)
          : Flowable.empty();
    }

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListOperation} stream to only the {@link RemoveFromList} operations.
     */
    static <E> Function<ObservableList.ListOperation<E>, Flowable<ObservableList.RemoveFromList<E>>> justRemoveFromListMutations() {
      return mutation -> mutation instanceof RemoveFromList
          ? Flowable.just((RemoveFromList) mutation)
          : Flowable.empty();
    }

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListOperation} stream to only the {@link ReplaceInList} operations.
     */
    static <E> Function<ObservableList.ListOperation<E>, Flowable<ObservableList.ReplaceInList<E>>> justReplaceInListMutations() {
      return mutation -> mutation instanceof ReplaceInList
          ? Flowable.just((ReplaceInList) mutation)
          : Flowable.empty();
    }
  }

  /** Represents one or more items being added to the list. */
  interface AddToList<E2> extends ListOperation {

    /** The item or items that were added to the list. */
    List<E2> items();

    /** The index range at which the items were added. */
    IndexRange indices();
  }

  /**
   * Represents one or more items being relocated to a new position within the list. This may be
   * explicit request of the list user, or a side-effect of an item being inserted or removed from
   * the middle or beginning of the list.
   */
  interface MoveInList<E2> extends ListOperation {
    /** The items that were moved within the list. */
    List<E2> items();

    /** The range of indices at which the items resided in the previous state of the list. */
    IndexRange previousIndicies();

    /** The range of indices at which the items now reside in the current state of the list. */
    IndexRange currentIndicies();
  }

  /** Represents one or more items being removed from the list. */
  interface RemoveFromList<E2> extends ListOperation {

    /** The items that were removed from the list.*/
    List<E2> items();

    /** The indices of the items in the previous state of the list. */
    IndexRange indices();
  }

  /**
   * Represents one or more items being replaced with new items at the same indices within the
   * list.
   */
  interface ReplaceInList<E2> extends ListOperation {
    /**
     * The items that were replaced by {@link #newItems()}. These items may still be located in
     * other positions within the list.
     */
    List<E2> replacedItems();

    /** The items that replaced {@link #replacedItems()}. */
    List<E2> newItems();

    /** The range of indices within the list whose values were replaced in this operation. */
    IndexRange indices();
  }

  interface IndexRange extends Countable, Iterable<Integer> {
    /**
     * The <b>inclusive</b> starting index of the range. Guaranteed to represent a valid index into
     * a list.
     */
    int start();

    /**
     * The <b>non-inclusive</b> ending index of the range. Not guaranteed to represent a valid
     * index into a list.
     */
    int end();

    /**
     * The <b>inclusive</b> end index of the range. Guaranteed to represent a valid index into a
     * list.
     */
    int endInclusive();

    /**
     * The number of indices contained within the range. Equivalent to
     * {@code {@link #end()} - {@link #start()}}.
     */
    int count();
  }
}
