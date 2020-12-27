package omnia.data.structure.observable

import io.reactivex.rxjava3.core.Observable
import omnia.data.structure.Set

interface ObservableSet<E> : ObservableDataStructure, Set<E> {
    override fun observe(): ObservableChannels<E>
    interface ObservableChannels<E> : ObservableDataStructure.ObservableChannels {
        override fun states(): Observable<out Set<E>>
        override fun mutations(): Observable<out MutationEvent<E>>
    }

    interface MutationEvent<E> : ObservableDataStructure.MutationEvent {
        override fun state(): Set<E>
        override fun operations(): Set<out SetOperation<E>>
    }

    interface SetOperation<E> {
        fun item(): E

        companion object {
            fun <E> justAddToSetOperations(
                    observable: Observable<out SetOperation<E>>): Observable<AddToSet<E>> {
                return observable.flatMap { mutation: SetOperation<E> -> if (mutation is AddToSet<*>) Observable.just(mutation as AddToSet<E>) else Observable.empty() }
            }

            fun <E> justRemoveFromSetOperations(
                    observable: Observable<out SetOperation<E>>): Observable<RemoveFromSet<E>> {
                return observable.flatMap { mutation: SetOperation<E> -> if (mutation is RemoveFromSet<*>) Observable.just(mutation as RemoveFromSet<E>) else Observable.empty() }
            }
        }
    }

    interface AddToSet<E> : SetOperation<E>
    interface RemoveFromSet<E> : SetOperation<E>
}