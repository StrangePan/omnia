package omnia.data.structure.rx;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.contract.Countable;
import omnia.data.structure.List;
import omnia.data.structure.mutable.MutableList;

public interface ObservableList<E> extends MutableList<E>, ObservableDataStructure<List<E>, List<ObservableList.ListMutation<E>>> {

  /**
   * Represents a mutation to a list data structure. Can be cast to a specific subtype using one
   * of the available static functions.
   */
  @SuppressWarnings("unused") // E unused, but required for type safety when upcasting
  interface ListMutation<E> {

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListMutation} stream to only the {@link AddToList} mutations.
     */
    static <E> Function<ListMutation<E>, Flowable<AddToList<E>>> justAddToListMutations() {
      return mutation -> mutation instanceof AddToList<?>
          ? Flowable.just((AddToList<E>) mutation)
          : Flowable.empty();
    }

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListMutation} stream to only the {@link MoveInList} mutations.
     */
    static <E> Function<ListMutation<E>, Flowable<MoveInList<E>>> justMoveInListMutations() {
      return mutation -> mutation instanceof MoveInList<?>
          ? Flowable.just((MoveInList<E>) mutation)
          : Flowable.empty();
    }

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListMutation} stream to only the {@link RemoveFromList} mutations.
     */
    static <E> Function<ListMutation<E>, Flowable<RemoveFromList<E>>> justRemoveFromListMutations() {
      return mutation -> mutation instanceof RemoveFromList<?>
          ? Flowable.just((RemoveFromList<E>) mutation)
          : Flowable.empty();
    }

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListMutation} stream to only the {@link ReplaceInList} mutations.
     */
    static <E> Function<ListMutation<E>, Flowable<ReplaceInList<E>>> justReplaceInListMutations() {
      return mutation -> mutation instanceof ReplaceInList<?>
          ? Flowable.just((ReplaceInList<E>) mutation)
          : Flowable.empty();
    }
  }

  /** Represents one or more items being added to the list. */
  interface AddToList<E> extends ListMutation<E> {

    /** The item or items that were added to the list. */
    List<E> items();

    /** The index range at which the items were added. */
    IndexRange indices();
  }

  /**
   * Represents one or more items being relocated to a new position within the list. This may be
   * explicit request of the list user, or a side-effect of an item being inserted or removed from
   * the middle or beginning of the list.
   */
  interface MoveInList<E> extends ListMutation<E> {
    /** The range of indices at which the items resided in the previous state of the list. */
    IndexRange previousIndicies();

    /** The range of indices at which the items now reside in the current state of the list. */
    IndexRange currentIndicies();

    /** The items that were moved within the list. */
    List<E> items();
  }

  /** Represents one or more items being removed from the list. */
  interface RemoveFromList<E> extends ListMutation<E> {

    /** The items that were removed from the list.*/
    List<E> items();

    /** The indices of the items in the previous state of the list. */
    IndexRange indices();
  }

  /**
   * Represents one or more items being replaced with new items at the same indices within the
   * list.
   */
  interface ReplaceInList<E> extends ListMutation<E> {
    /** The range of indices within the list whose values were replaced in this operation. */
    IndexRange indices();

    /**
     * The items that were replaced by {@link #newItems()}. These items may still be located in
     * other positions within the list.
     */
    List<E> replacedItems();

    /** The items that replaced {@link #replacedItems()}. */
    List<E> newItems();
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
