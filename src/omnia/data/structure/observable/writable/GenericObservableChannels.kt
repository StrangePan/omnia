package omnia.data.structure.observable.writable

import io.reactivex.rxjava3.core.Observable
import omnia.data.structure.observable.ObservableDataStructure

internal open class GenericObservableChannels<StateType, MutationEventType : ObservableDataStructure.MutationEvent> protected constructor(
        private val states: Observable<StateType>, private val mutations: Observable<MutationEventType>) : ObservableDataStructure.ObservableChannels {
    override fun states(): Observable<StateType> {
        return states
    }

    override fun mutations(): Observable<MutationEventType> {
        return mutations
    }
}