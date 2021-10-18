package omnia.data.structure.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapNotNull
import omnia.data.structure.Set

interface ObservableSet<E : Any> : ObservableDataStructure, Set<E> {

  override val observables: Observables<E>

  interface Observables<E : Any> : ObservableDataStructure.Observables {

    override val states: Observable<Set<E>>

    override val mutations: Observable<MutationEvent<E>>
  }

  interface MutationEvent<E : Any> : ObservableDataStructure.MutationEvent {

    override val state: Set<E>

    override val operations: Set<out SetOperation<E>>
  }

  interface SetOperation<E : Any> {

    val item: E

    companion object {

      fun <E : Any> justAddToSetOperations(operations: Observable<SetOperation<E>>):
          Observable<AddToSet<E>> {
        return operations.mapNotNull { it as AddToSet<E> }
      }

      fun <E : Any> justRemoveFromSetOperations(operations: Observable<SetOperation<E>>):
          Observable<RemoveFromSet<E>> {
        return operations.mapNotNull { it as RemoveFromSet<E> }
      }
    }
  }

  interface AddToSet<E : Any> : SetOperation<E>

  interface RemoveFromSet<E : Any> : SetOperation<E>
}