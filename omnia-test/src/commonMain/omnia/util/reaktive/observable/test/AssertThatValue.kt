package omnia.util.reaktive.observable.test

import com.badoo.reaktive.test.observable.TestObservableObserver
import omnia.util.test.assertGreater
import omnia.util.test.assertGreaterOrEqual
import omnia.util.test.fluent.Assertion
import omnia.util.test.fluent.Assertion.Companion.assertThat

fun <T> TestObservableObserver<T>.assertThatValue(index: Int = 0) =
  this.assertThatValue(index) { it }

fun <T, R> TestObservableObserver<T>.assertThatValue(mapper: (T) -> R) =
  this.assertThatValue(0, mapper)

fun <T, R> TestObservableObserver<T>.assertThatValue(index: Int, mapper: (T) -> R): Assertion<R> {
  assertGreaterOrEqual(0, index)
  assertGreater(index, this.values.size)
  return assertThat(mapper(this.values[index]))
}