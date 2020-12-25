package omnia.data.structure.observable;

import static java.util.Objects.requireNonNull;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.SingleSubject;
import java.util.Objects;
import omnia.data.structure.tuple.Couple;

public final class ObservableState<T> {

  private final PublishSubject<Function<T, T>> mutations = PublishSubject.create();
  private final Observable<T> observableState;

  public static <T> ObservableState<T> create(T initialState) {
    return new ObservableState<>(initialState);
  }

  private ObservableState(T initialState) {
    observableState =
        mutations.hide()
            .scan(initialState, (state, mutation) -> Objects.requireNonNull(mutation.apply(state)))
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0);
  }

  /** A hot observable for the stateful object. */
  public Observable<T> observe() {
    return observableState;
  }

  /** Mutates the state asynchronously and returns the result of the mutation. */
  public Single<T> mutate(Function<? super T, ? extends T> mutator) {
    requireNonNull(mutator);
    SingleSubject<T> result = SingleSubject.create();
    mutations.onNext(
        state -> {
          T newState;
          try {
             newState = Objects.requireNonNull(mutator.apply(state));
          } catch (Throwable t) {
            result.onError(t);
            return state;
          }
          result.onSuccess(newState);
          return newState;
        });
    return result;
  }

  /**
   * Mutates the state asynchronously and returns the result of the mutation plus an extra value
   * as returned by the mutator. This allows the mutator to return extra information in addition
   * to the new state, such as a diff or generated data.
   */
  public <R> Single<Couple<? extends T, ? extends R>> mutateAndReturn(
      Function<? super T, ? extends Couple<? extends T, ? extends R>> mutator) {
    requireNonNull(mutator);
    SingleSubject<Couple<? extends T, ? extends R>> result = SingleSubject.create();
    mutations.onNext(
        state -> {
          Couple<? extends T, ? extends R> newState;
          try {
            newState = Objects.requireNonNull(mutator.apply(state));
          } catch (Throwable t) {
            result.onError(t);
            return state;
          }
          result.onSuccess(newState);
          return newState.first();
        });
    return result;
  }
}
