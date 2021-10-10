package omnia.data.structure.observable.writable

import com.badoo.reaktive.observable.Observable
import omnia.data.structure.observable.ObservableDataStructure

internal open class GenericObservableChannels<
    StateType : Any,
    MutationEventType : ObservableDataStructure.MutationEvent>
protected constructor(
    private val states: Observable<StateType>,
    private val mutations: Observable<MutationEventType>) :
    ObservableDataStructure.ObservableChannels {

  override fun states(): Observable<StateType> {
    return states
  }

  override fun mutations(): Observable<MutationEventType> {
    return mutations
  }
}