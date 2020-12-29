package omnia.data.structure.observable

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.SingleSubject
import java.util.Objects
import omnia.data.structure.tuple.Couple

class ObservableState<T> private constructor(initialState: T) {

  private val mutations: PublishSubject<Function<T, T>> = PublishSubject.create()
  private val observableState: Observable<T> =
    mutations.hide()
      .scan(initialState) { state, mutation -> mutation.apply(state) }
      .distinctUntilChanged()
      .replay(1)
      .autoConnect(0)

  /** A hot observable for the stateful object.  */
  fun observe(): Observable<T> {
    return observableState
  }

  /** Mutates the state asynchronously and returns the result of the mutation.  */
  fun mutate(mutator: Function<in T, out T>): Single<T> {
    Objects.requireNonNull(mutator)
    val result: SingleSubject<T> = SingleSubject.create()
    mutations.onNext(
      Function { state: T ->
        val newState: T = try {
          Objects.requireNonNull(mutator.apply(state))
        } catch (t: Throwable) {
          result.onError(t)
          state
        }
        result.onSuccess(newState)
        newState
      })
    return result
  }

  /**
   * Mutates the state asynchronously and returns the result of the mutation plus an extra value
   * as returned by the mutator. This allows the mutator to return extra information in addition
   * to the new state, such as a diff or generated data.
   */
  fun <R> mutateAndReturn(
    mutator: Function<in T, out Couple<out T, out R>>
  ): Single<Couple<out T, out R>> {
    Objects.requireNonNull(mutator)
    val result: SingleSubject<Couple<out T, out R>> = SingleSubject.create()
    mutations.onNext(
      Function { state: T ->
        val newState: Couple<out T, out R> = try {
          Objects.requireNonNull(mutator.apply(state))
        } catch (t: Throwable) {
          result.onError(t)
          return@Function state
        }
        result.onSuccess(newState)
        newState.first()
      })
    return result
  }

  companion object {

    @kotlin.jvm.JvmStatic
    fun <T> create(initialState: T): ObservableState<T> {
      return ObservableState(initialState)
    }
  }
}