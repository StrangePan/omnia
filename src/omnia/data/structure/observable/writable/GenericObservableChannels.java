package omnia.data.structure.observable.writable;

import io.reactivex.rxjava3.core.Observable;
import omnia.data.structure.observable.ObservableDataStructure;

class GenericObservableChannels<
        StateType, MutationEventType extends ObservableDataStructure.MutationEvent>
    implements ObservableDataStructure.ObservableChannels {

  private final Observable<StateType> states;
  private final Observable<MutationEventType> mutations;

  protected GenericObservableChannels(
      Observable<StateType> states, Observable<MutationEventType> mutations) {
    this.states = states;
    this.mutations = mutations;
  }

  @Override
  public Observable<StateType> states() {
    return states;
  }

  @Override
  public Observable<MutationEventType> mutations() {
    return mutations;
  }
}
