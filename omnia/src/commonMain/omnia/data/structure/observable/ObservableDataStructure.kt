package omnia.data.structure.observable

import com.badoo.reaktive.observable.Observable
import omnia.data.structure.Collection

/**
 * A mutable data structure whose state changes can be observed. Observers can subscribe to state
 * changes and can subscribe to receive summaries of the operations that occurred between state
 * changes, allowing subscribers to process "diffs" between states.
 */
interface ObservableDataStructure {

  /**
   * Returns an [ObservableChannels] with which observers can choose which channel to
   * subscribe to.
   */
  fun observe(): ObservableChannels

  /**
   * A condense view of the types of observable channels available to subscribers. This encapsulates
   * the Rx-related methods into a contained interface so as not to pollute the namespace of the
   * data structure.
   */
  interface ObservableChannels {

    /**
     * Emits an immutable copy of the data structure whenever its state changes.
     *
     *
     * At time of subscription, each new subscriber receives the last known state of teh
     * data structure. New subscribers should ignore this first emission if they wish to only be
     * notified of changes.
     */
    fun states(): Observable<*>

    /**
     * Emits both an immutable copy of the data structure whenever its state changes, as well
     * as a description of the mutation or operations that describe the changes between the data
     * structure's previously emitted state and the current state.
     *
     *
     * At time of subscription, each new subscriber receives the last known state of the data
     * structure, as well as a mutation equivalent of populating an empty data structure with the
     * contents of current state.
     */
    fun mutations(): Observable<MutationEvent>
  }

  /**
   * An event representing a mutation in the data structure. Mutation events contain the new state
   * of the data structure as well as a collections of operations that, when applied to the previous
   * known state of the data structure, will result in the state contained in this event.
   *
   *
   * The semantics of each specific operation type is defined by the data structure withi which
   * it is associated.
   */
  interface MutationEvent {

    /** The state of the data structure after this mutation event.  */
    fun state(): Any

    /**
     * The collection of operations that this mutation event comprises. These operations describe
     * the changes between the data structure's previous state state and the state associated with
     * this event. Applying these operations to the previous state will result in the current state.
     *
     *
     * See the specific data structure's documentation for the semantics of how each operation
     * is to be applied.
     */
    fun operations(): Collection<*>
  }
}