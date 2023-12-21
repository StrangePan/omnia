package omnia.data.structure.observable

import com.badoo.reaktive.completable.blockingAwait
import com.badoo.reaktive.observable.firstOrError
import com.badoo.reaktive.single.asCompletable
import com.badoo.reaktive.single.blockingGet
import com.badoo.reaktive.test.observable.assertNotComplete
import com.badoo.reaktive.test.observable.assertValue
import com.badoo.reaktive.test.observable.assertValues
import com.badoo.reaktive.test.observable.test
import com.badoo.reaktive.test.single.assertSuccess
import com.badoo.reaktive.test.single.test
import kotlin.test.Test
import omnia.data.structure.observable.ObservableState.Companion.create
import omnia.data.structure.tuple.Tuple
import omnia.util.reaktive.observable.test.assertThatValue
import omnia.util.reaktive.observable.test.assertValueCount
import omnia.util.test.fluent.andThat
import omnia.util.test.fluent.isEqualTo
import omnia.util.test.fluent.isSameAs

class ObservableStateTest {

  private val underTest = create(0)

  @Test
  fun observe_whenInit_emitsInitialValue() {
    underTest.observe().test().assertValue(0)
  }

  @Test
  fun observe_whenMutated_emitsMutatedValue() {
    underTest.mutate { i: Int -> i + 1 }.asCompletable().blockingAwait()
    underTest.observe().test().assertValue(1)
  }

  @Test
  fun observe_thenMutate_emitsBothValues() {
    val observer = underTest.observe().test()
    underTest.mutate { i: Int -> i + 1 }.asCompletable().blockingAwait()
    observer.assertValues(0, 1)
  }

  @Test
  fun mutate_returnsNewValue() {
    underTest.mutate { i: Int -> i + 1 }.test().assertSuccess(1)
  }

  @Test
  fun mutateAndReturn_whenReturnsOtherValue_returnsOtherValue() {
    underTest.mutateAndReturn<Any> { i: Int -> Tuple.of(i + 1, 100) }.test()
      .assertSuccess(Tuple.of(1, 100))
  }

  @Test
  fun observe_thenMutateAndReturn_returnsBothValues() {
    val observer = underTest.observe().test()
    underTest.mutateAndReturn<Any> { i: Int -> Tuple.of(i + 1, 100) }.asCompletable()
      .blockingAwait()
    observer.assertValues(0, 1)
  }

  @Test
  fun mutate_whenEquivalent_returnsOriginalValue_doesNotEmitNewValues() {
    val underTest = create("hello world")
    val original = underTest.observe().firstOrError().blockingGet()
    val observer = underTest.observe().test().assertValueCount(1).assertNotComplete()
    underTest.mutate { "hello world" }.test().assertThatValue().isSameAs(original)
    observer.assertValueCount(1).assertNotComplete()
  }

  @Test
  fun mutateAndReturn_whenEquivalent_returnsOriginalValue_doesNotEmitNewValues() {
    val underTest = create("hello world")
    val original = underTest.observe().firstOrError().blockingGet()
    val observer = underTest.observe().test().assertValueCount(1).assertNotComplete()
    underTest.mutateAndReturn { Tuple.of("hello world", "some other value") }.test().assertThatValue()
      .andThat({ it.first }) { it.isSameAs(original) }
      .andThat({ it.second }) { it.isEqualTo("some other value") }
    observer.assertValueCount(1).assertNotComplete()
  }
}
