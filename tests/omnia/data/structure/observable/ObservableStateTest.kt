package omnia.data.structure.observable

import omnia.data.structure.observable.ObservableState.Companion.create
import omnia.data.structure.tuple.Tuple
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ObservableStateTest {
  private val underTest = create(0)
  @Test
  fun observe_whenInit_emitsInitialValue() {
    underTest.observe().test().assertValue(0)
  }

  @Test
  fun observe_whenMutated_emitsMutatedValue() {
    underTest.mutate { i: Int -> i + 1 }.ignoreElement().blockingAwait()
    underTest.observe().test().assertValue(1)
  }

  @Test
  fun observe_thenMutate_emitsBothValues() {
    val observer = underTest.observe().test()
    underTest.mutate { i: Int -> i + 1 }.ignoreElement().blockingAwait()
    observer.assertValues(0, 1)
  }

  @Test
  fun mutate_returnsNewValue() {
    underTest.mutate { i: Int -> i + 1 }.test().assertValue(1)
  }

  @Test
  fun mutateAndReturn_whenReturnsOtherValue_returnsOtherValue() {
    underTest.mutateAndReturn<Any> { i: Int -> Tuple.of(i + 1, 100) }.test().assertValue(Tuple.of(1, 100))
  }

  @Test
  fun observe_thenMutateAndReturn_returnsBothValues() {
    val observer = underTest.observe().test()
    underTest.mutateAndReturn<Any> { i: Int -> Tuple.of(i + 1, 100) }.ignoreElement().blockingAwait()
    observer.assertValues(0, 1)
  }
}