package omnia.data.structure.observable.writable

import omnia.data.structure.Collection
import omnia.data.structure.observable.ObservableDataStructure

internal open class GenericMutationEvent<StateType : Any, OperationsType : Collection<*>> protected constructor(
  override val state: StateType,
  override val operations: OperationsType
) : ObservableDataStructure.MutationEvent