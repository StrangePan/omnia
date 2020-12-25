package omnia.data.structure.observable;

import io.reactivex.rxjava3.observers.TestObserver;
import omnia.data.structure.tuple.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class ObservableStateTest {

  private final ObservableState<Integer> underTest = ObservableState.create(0);

  @Test
  public void observe_whenInit_emitsInitialValue() {
    underTest.observe().test().assertValue(0);
  }

  @Test
  public void observe_whenMutated_emitsMutatedValue() {
    underTest.mutate(i -> i + 1).ignoreElement().blockingAwait();

    underTest.observe().test().assertValue(1);
  }

  @Test
  public void observe_thenMutate_emitsBothValues() {
    TestObserver<Integer> observer = underTest.observe().test();

    underTest.mutate(i -> i + 1).ignoreElement().blockingAwait();

    observer.assertValues(0, 1);
  }

  @Test
  public void mutate_returnsNewValue() {
    underTest.mutate(i -> i + 1).test().assertValue(1);
  }

  @Test
  public void mutateAndReturn_whenReturnsOtherValue_returnsOtherValue() {
    underTest.mutateAndReturn(i -> Tuple.of(i + 1, 100)).test().assertValue(Tuple.of(1, 100));
  }

  @Test
  public void observe_thenMutateAndReturn_returnsBothValues() {
    TestObserver<Integer> observer = underTest.observe().test();

    underTest.mutateAndReturn(i -> Tuple.of(i + 1, 100)).ignoreElement().blockingAwait();

    observer.assertValues(0, 1);
  }
}
