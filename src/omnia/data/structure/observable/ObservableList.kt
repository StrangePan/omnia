package omnia.data.structure.observable

import io.reactivex.rxjava3.core.Observable
import omnia.data.structure.IntRange
import omnia.data.structure.List

interface ObservableList<E> : List<E>, ObservableDataStructure {
    override fun observe(): ObservableChannels<E>

    interface ObservableChannels<E> : ObservableDataStructure.ObservableChannels {
        override fun states(): Observable<out List<E>>
        override fun mutations(): Observable<out MutationEvent<E>>
    }

    interface MutationEvent<E> : ObservableDataStructure.MutationEvent {
        override fun state(): List<E>
        override fun operations(): List<out ListOperation<E>>
    }

    /**
     * Represents a mutation to a list data structure. Can be cast to a specific subtype using one
     * of the available static functions.
     */
    interface ListOperation<E> {
        companion object {
            /**
             * Returns a mapping function for use in [Observable.flatMap] operations that
             * reduces a [ListOperation] stream to only the [AddToList] operations.
             */
            fun <E> justAddToListOperations(
                    observable: Observable<out ListOperation<E>>): Observable<AddToList<E>> {
                return observable.flatMap { mutation: ListOperation<E> -> if (mutation is AddToList<*>) Observable.just(mutation as AddToList<E>) else Observable.empty() }
            }

            /**
             * Returns a mapping function for use in [Observable.flatMap] operations that
             * reduces a [ListOperation] stream to only the [MoveInList] operations.
             */
            fun <E> justMoveInListOperations(
                    observable: Observable<out ListOperation<E>>): Observable<MoveInList<E>> {
                return observable.flatMap { mutation: ListOperation<E> -> if (mutation is MoveInList<*>) Observable.just(mutation as MoveInList<E>) else Observable.empty() }
            }

            /**
             * Returns a mapping function for use in [Observable.flatMap] operations that
             * reduces a [ListOperation] stream to only the [RemoveFromList] operations.
             */
            fun <E> justRemoveFromListOperations(
                    observable: Observable<out ListOperation<E>>): Observable<RemoveFromList<E>> {
                return observable.flatMap { mutation: ListOperation<E> -> if (mutation is RemoveFromList<*>) Observable.just(mutation as RemoveFromList<E>) else Observable.empty() }
            }

            /**
             * Returns a mapping function for use in [Observable.flatMap] operations that
             * reduces a [ListOperation] stream to only the [ReplaceInList] operations.
             */
            fun <E> justReplaceInListOperations(
                    observable: Observable<out ListOperation<E>>): Observable<ReplaceInList<E>> {
                return observable.flatMap { mutation: ListOperation<E> -> if (mutation is ReplaceInList<*>) Observable.just(mutation as ReplaceInList<E>) else Observable.empty() }
            }
        }
    }

    /** Represents one or more items being added to the list.  */
    interface AddToList<E> : ListOperation<E> {
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
    interface MoveInList<E> : ListOperation<E> {
        /** The items that were moved within the list.  */
        fun items(): List<E>

        /** The range of indices at which the items resided in the previous state of the list.  */
        fun previousIndices(): IntRange

        /** The range of indices at which the items now reside in the current state of the list.  */
        fun currentIndices(): IntRange
    }

    /** Represents one or more items being removed from the list.  */
    interface RemoveFromList<E> : ListOperation<E> {
        /** The items that were removed from the list. */
        fun items(): List<E>

        /** The indices of the items in the previous state of the list.  */
        fun indices(): IntRange
    }

    /**
     * Represents one or more items being replaced with new items at the same indices within the
     * list.
     */
    interface ReplaceInList<E> : ListOperation<E> {
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