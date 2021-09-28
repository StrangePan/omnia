package omnia.data.structure.observable

import io.reactivex.rxjava3.core.Observable
import omnia.data.structure.Set

interface ObservableSet<E : Any> : ObservableDataStructure, Set<E> {

  override fun observe(): ObservableChannels<E>

  interface ObservableChannels<E : Any> : ObservableDataStructure.ObservableChannels {

    override fun states(): Observable<out Set<E>>

    override fun mutations(): Observable<out MutationEvent<E>>
  }

  interface MutationEvent<E : Any> : ObservableDataStructure.MutationEvent {

    override fun state(): Set<E>

    override fun operations(): Set<out SetOperation<E>>
  }

  interface SetOperation<E : Any> {

    fun item(): E

    companion object {

      fun <E : Any> justAddToSetOperations(
        observable: Observable<out SetOperation<E>>
      ): Observable<AddToSet<E>> {
        return observable.flatMap { mutation: SetOperation<E> ->
          if (mutation is AddToSet<*>) Observable.just(
            mutation as AddToSet<E>
          ) else Observable.empty()
        }
      }

      fun <E : Any> justRemoveFromSetOperations(
        observable: Observable<out SetOperation<E>>
      ): Observable<RemoveFromSet<E>> {
        return observable.flatMap { mutation: SetOperation<E> ->
          if (mutation is RemoveFromSet<*>) Observable.just(
            mutation as RemoveFromSet<E>
          ) else Observable.empty()
        }
      }
    }
  }

  interface AddToSet<E : Any> : SetOperation<E>

  interface RemoveFromSet<E : Any> : SetOperation<E>
}