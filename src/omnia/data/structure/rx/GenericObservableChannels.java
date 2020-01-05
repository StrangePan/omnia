package omnia.data.structure.rx;

import io.reactivex.Flowable;

class GenericObservableChannels<
        StateType, MutationEventType extends ObservableDataStructure.MutationEvent>
    implements ObservableDataStructure.ObservableChannels {

  private final Flowable<StateType> states;
  private final Flowable<MutationEventType> mutations;

  protected GenericObservableChannels(
      Flowable<StateType> states, Flowable<MutationEventType> mutations) {
    this.states = states;
    this.mutations = mutations;
  }

  @Override
  public Flowable<StateType> states() {
    return states;
  }

  @Override
  public Flowable<MutationEventType> mutations() {
    return mutations;
  }
}
