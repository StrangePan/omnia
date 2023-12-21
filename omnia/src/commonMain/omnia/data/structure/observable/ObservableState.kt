package omnia.data.structure.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.autoConnect
import com.badoo.reaktive.observable.distinctUntilChanged
import com.badoo.reaktive.observable.firstOrError
import com.badoo.reaktive.observable.replay
import com.badoo.reaktive.observable.scan
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.subject.publish.PublishSubject
import com.badoo.reaktive.subject.replay.ReplaySubject
import omnia.data.structure.tuple.Couple
import omnia.data.structure.tuple.Tuple

class ObservableState<T : Any> private constructor(initialState: T) {

  private val mutations: PublishSubject<(T) -> T> = PublishSubject()
  private val observableState: Observable<T> =
    mutations
      .scan(initialState) { state, mutation -> mutation(state) }
      .distinctUntilChanged()
      .replay(1)
      .autoConnect(0)

  /** A hot observable for the stateful object.  */
  fun observe(): Observable<T> {
    return observableState
  }

  /** Mutates the state asynchronously and returns the result of the mutation.  */
  fun mutate(mutator: (T) -> T): Single<T> {
    return mutateAndReturn { original -> Tuple.of<T, Any?>(mutator(original), null) }
      .map(Couple<out T, out Any?>::first)
  }

  /**
   * Mutates the state asynchronously and returns the result of the mutation plus an extra value
   * as returned by the mutator. This allows the mutator to return extra information in addition
   * to the new state, such as a diff or generated data.
   */
  fun <R> mutateAndReturn(mutator: (T) -> Couple<out T, out R>): Single<Couple<out T, out R>> {
    val result: ReplaySubject<Couple<out T, out R>> = ReplaySubject(1)
    mutations.onNext { originalState: T ->
      val newState: Couple<out T, out R> = try {
        mutator(originalState)
      } catch (t: Throwable) {
        result.onError(t)
        return@onNext originalState
      }
      if (newState.first == originalState) {
        result.onNext(Tuple.of(originalState, newState.second))
      } else {
        result.onNext(newState)
      }
      newState.first
    }
    return result.firstOrError()
  }

  companion object {
    fun <T : Any> create(initialState: T): ObservableState<T> {
      return ObservableState(initialState)
    }
  }
}
