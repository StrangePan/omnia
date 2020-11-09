package omnia.data.structure.observable;

import static java.util.Objects.requireNonNull;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.SingleSubject;
import java.util.Objects;

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
            .startWith(initialState)
            .replay(1)
            .autoConnect(0);
  }

  /** A hot observable for the stateful object. */
  public Observable<T> observe() {
    return observableState;
  }

  /** Mutates the state asynchronously and returns the result of the mutation. */
  public Single<T> mutate(Function<T, T> mutator) {
    requireNonNull(mutator);
    SingleSubject<T> result = SingleSubject.create();
    mutations.onNext(
        state -> {
          T newState = Objects.requireNonNull(mutator.apply(state));
          result.onSuccess(newState);
          return newState;
        });
    return result;
  }
}
