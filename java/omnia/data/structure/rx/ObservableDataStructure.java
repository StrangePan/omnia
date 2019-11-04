package omnia.data.structure.rx;

import io.reactivex.Flowable;

/**
 * A mutable data structure whose state changes can be observed using RxJava. Observers can
 * subscribe to state changes and can subscribe to receive summaries of the mutations that occured
 * between state changes, allowing subscribers to process "diffs" between states.
 *
 * @param <StateType> The type representing each distinct state of the data structure. This is often
 *     an immutable copy of the data structure.
 * @param <MutationType> The type representing mutations for this data structure. This is typically
 *     an empty interface with multiple subclasses containing mutation-specific parameters.
 */
public interface ObservableDataStructure<StateType, MutationType> {

  /**
   * Returns an {@link ObservableChannels} with which observers can choose which channel to
   * subscribe to.
   */
  ObservableChannels<StateType, MutationType> observe();

  /**
   * A condense view of the types of observable channels available to subscribers. This encapsulates
   * the Rx-related methods into a contained interface so as not to pollute the namespace of the
   * data structure.
   */
  interface ObservableChannels<StateType, MutationType> {

    /**
     * Emits an immutable copy of the data structure whenever its state changes.
     *
     * <p>At time of subscription, each new subscriber receives the last known state of teh
     * data structure. New subscribers should ignore this first emission if they wish to only be
     * notified of changes.
     */
    Flowable<? extends StateType> states();

    /**
     * Emits both an immutable copy of the data structure whenever its state changes, as well
     * as a description of the mutation or mutations that describe the changes between the data
     * structure's previously emitted state and the current state.
     *
     * <p>At time of subscription, each new subscriber receives the last known state of the data
     * structure, as well as a mutation equivalent of populating an empty data structure with the
     * contents of current state.
     */
    Flowable<? extends MutationEvent<? extends StateType, ? extends MutationType>> mutations();
  }

  /**
   * An event representing a mutation in the data structure. Mutation events contain the new state
   * of the data structure as well as a list of mutations that, when applied <b>in order</b> to the
   * previous known state of the data structure will, result in the current state.
   *
   * <p>The semantics of each specific mutation type is governed by the semantics of the data
   * structure and valid operations available to it.
   */
  interface MutationEvent<StateType, MutationType> {

    /** The state of the data structure after this mutation event. */
    StateType state();

    /**
     * The mutation that triggered this mutation event. This mutation describes the
     * changes between the data structure's previous state state and current state. Applying this
     * mutation to the previous state will result in the current state.
     */
    MutationType mutations();
  }
}
