package omnia.data.structure.observable.writable

import com.badoo.reaktive.observable.Observable
import omnia.data.structure.observable.ObservableDataStructure

internal open class GenericObservables<
    StateType : Any,
    MutationEventType : ObservableDataStructure.MutationEvent>
protected constructor(
    override val states: Observable<StateType>,
    override val mutations: Observable<MutationEventType>) :
    ObservableDataStructure.Observables {
}