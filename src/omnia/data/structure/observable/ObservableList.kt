package omnia.data.structure.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapNotNull
import omnia.data.structure.IntRange
import omnia.data.structure.List

interface ObservableList<E : Any> : List<E>, ObservableDataStructure {

  override fun observe(): ObservableChannels<E>

  interface ObservableChannels<E : Any> : ObservableDataStructure.ObservableChannels {

    override fun states(): Observable<List<E>>

    override fun mutations(): Observable<MutationEvent<E>>
  }

  interface MutationEvent<E : Any> : ObservableDataStructure.MutationEvent {

    override fun state(): List<E>

    override fun operations(): List<out ListOperation<E>>
  }

  /**
   * Represents a mutation to a list data structure. Can be cast to a specific subtype using one
   * of the available static functions.
   */
  interface ListOperation<E : Any> {

    companion object {

      /**
       * Returns a mapping function for use in [Observable.flatMap] operations that
       * reduces a [ListOperation] stream to only the [AddToList] operations.
       */
      fun <E : Any> justAddToListOperations(operations: Observable<ListOperation<E>>):
          Observable<AddToList<E>> {
        return operations.mapNotNull { it as? AddToList<E> }
      }

      /**
       * Returns a mapping function for use in [Observable.flatMap] operations that
       * reduces a [ListOperation] stream to only the [MoveInList] operations.
       */
      fun <E : Any> justMoveInListOperations(operations: Observable<ListOperation<E>>):
          Observable<MoveInList<E>> {
        return operations.mapNotNull { it as MoveInList<E> }
      }

      /**
       * Returns a mapping function for use in [Observable.flatMap] operations that
       * reduces a [ListOperation] stream to only the [RemoveFromList] operations.
       */
      fun <E : Any> justRemoveFromListOperations(operations: Observable<ListOperation<E>>):
          Observable<RemoveFromList<E>> {
        return operations.mapNotNull { it as RemoveFromList<E> }
      }

      /**
       * Returns a mapping function for use in [Observable.flatMap] operations that
       * reduces a [ListOperation] stream to only the [ReplaceInList] operations.
       */
      fun <E : Any> justReplaceInListOperations(operations: Observable<ListOperation<E>>):
          Observable<ReplaceInList<E>> {
        return operations.mapNotNull { it as ReplaceInList<E> }
      }
    }
  }

  /** Represents one or more items being added to the list.  */
  interface AddToList<E : Any> : ListOperation<E> {

    /** The item or items that were added to the list.  */
    fun items(): List<E>

    /** The index range at which the items were added.  */
    fun indices(): IntRange
  }

  /**
   * Represents one or more items being relocated to a new position within the list. This may be
   * explicit request of the list user, or a side-effect of an item being inserted or removed from
   * the middle or beginning of the list.
   */
  interface MoveInList<E : Any> : ListOperation<E> {

    /** The items that were moved within the list.  */
    fun items(): List<E>

    /** The range of indices at which the items resided in the previous state of the list.  */
    fun previousIndices(): IntRange

    /** The range of indices at which the items now reside in the current state of the list.  */
    fun currentIndices(): IntRange
  }

  /** Represents one or more items being removed from the list.  */
  interface RemoveFromList<E : Any> : ListOperation<E> {

    /** The items that were removed from the list. */
    fun items(): List<E>

    /** The indices of the items in the previous state of the list.  */
    fun indices(): IntRange
  }

  /**
   * Represents one or more items being replaced with new items at the same indices within the
   * list.
   */
  interface ReplaceInList<E : Any> : ListOperation<E> {

    /**
     * The items that were replaced by [newItems]. These items may still be located in
     * other positions within the list.
     */
    fun replacedItems(): List<E>

    /** The items that replaced [replacedItems].  */
    fun newItems(): List<E>

    /** The range of indices within the list whose values were replaced in this operation.  */
    fun indices(): IntRange
  }
}