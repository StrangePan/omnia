package omnia.data.structure.observable.writable

import omnia.data.structure.Collection
import omnia.data.structure.observable.ObservableDataStructure

internal open class GenericMutationEvent<StateType : Any, OperationsType : Collection<*>> protected constructor(private val state: StateType, private val operations: OperationsType) : ObservableDataStructure.MutationEvent {
    override fun state(): StateType {
        return state
    }

    override fun operations(): OperationsType {
        return operations
    }
}