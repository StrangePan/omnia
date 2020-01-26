package omnia.data.structure.observable;

import omnia.data.structure.Collection;

class GenericMutationEvent<StateType, OperationsType extends Collection<?>>
    implements ObservableDataStructure.MutationEvent {
  private final StateType state;
  private final OperationsType operations;

  protected GenericMutationEvent(StateType state, OperationsType operations) {
    this.state = state;
    this.operations = operations;
  }

  @Override
  public StateType state() {
    return state;
  }

  @Override
  public OperationsType operations() {
    return operations;
  }
}
