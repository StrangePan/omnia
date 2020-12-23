package omnia.data.structure.observable;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import omnia.data.structure.IntRange;
import omnia.data.structure.List;

public interface ObservableList<E> extends List<E>, ObservableDataStructure {

  @Override
  ObservableChannels<E> observe();

  interface ObservableChannels<E> extends ObservableDataStructure.ObservableChannels {

    @Override
    Flowable<? extends List<E>> states();

    @Override
    Flowable<? extends ObservableList.MutationEvent<E>> mutations();
  }

  interface MutationEvent<E> extends ObservableDataStructure.MutationEvent {

    @Override
    List<E> state();

    @Override
    List<? extends ObservableList.ListOperation<E>> operations();
  }

  /**
   * Represents a mutation to a list data structure. Can be cast to a specific subtype using one
   * of the available static functions.
   */
  interface ListOperation<E> {

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListOperation} stream to only the {@link AddToList} operations.
     */
    static <E> Flowable<AddToList<E>> justAddToListOperations(
        Flowable<? extends ListOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof AddToList<?>
              ? Flowable.just((AddToList<E>) mutation)
              : Flowable.empty());
    }

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListOperation} stream to only the {@link MoveInList} operations.
     */
    static <E> Flowable<MoveInList<E>> justMoveInListOperations(
        Flowable<? extends ListOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof MoveInList<?>
              ? Flowable.just((MoveInList<E>) mutation)
              : Flowable.empty());
    }

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListOperation} stream to only the {@link RemoveFromList} operations.
     */
    static <E> Flowable<RemoveFromList<E>> justRemoveFromListOperations(
        Flowable<? extends ListOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof RemoveFromList<?>
              ? Flowable.just((RemoveFromList<E>) mutation)
              : Flowable.empty());
    }

    /**
     * Returns a mapping function for use in {@link Flowable#flatMap(Function)} operations that
     * reduces a {@link ListOperation} stream to only the {@link ReplaceInList} operations.
     */
    static <E> Flowable<ReplaceInList<E>> justReplaceInListOperations(
        Flowable<? extends ListOperation<E>> flowable) {
      return flowable.flatMap(
          mutation -> mutation instanceof ReplaceInList<?>
              ? Flowable.just((ReplaceInList<E>) mutation)
              : Flowable.empty());
    }
  }

  /** Represents one or more items being added to the list. */
  interface AddToList<E> extends ListOperation<E> {

    /** The item or items that were added to the list. */
    List<E> items();

    /** The index range at which the items were added. */
    IntRange indices();
  }

  /**
   * Represents one or more items being relocated to a new position within the list. This may be
   * explicit request of the list user, or a side-effect of an item being inserted or removed from
   * the middle or beginning of the list.
   */
  interface MoveInList<E> extends ListOperation<E> {
    /** The items that were moved within the list. */
    List<E> items();

    /** The range of indices at which the items resided in the previous state of the list. */
    IntRange previousIndices();

    /** The range of indices at which the items now reside in the current state of the list. */
    IntRange currentIndices();
  }

  /** Represents one or more items being removed from the list. */
  interface RemoveFromList<E> extends ListOperation<E> {

    /** The items that were removed from the list.*/
    List<E> items();

    /** The indices of the items in the previous state of the list. */
    IntRange indices();
  }

  /**
   * Represents one or more items being replaced with new items at the same indices within the
   * list.
   */
  interface ReplaceInList<E> extends ListOperation<E> {
    /**
     * The items that were replaced by {@link #newItems()}. These items may still be located in
     * other positions within the list.
     */
    List<E> replacedItems();

    /** The items that replaced {@link #replacedItems()}. */
    List<E> newItems();

    /** The range of indices within the list whose values were replaced in this operation. */
    IntRange indices();
  }

}
