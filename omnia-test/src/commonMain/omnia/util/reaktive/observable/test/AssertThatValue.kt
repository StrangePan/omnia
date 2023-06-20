package omnia.util.reaktive.observable.test

import com.badoo.reaktive.test.maybe.TestMaybeObserver
import com.badoo.reaktive.test.maybe.assertSuccess
import com.badoo.reaktive.test.observable.TestObservableObserver
import com.badoo.reaktive.test.single.TestSingleObserver
import com.badoo.reaktive.test.single.assertSuccess
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

fun <T> TestSingleObserver<T>.assertThatValue() =
  this.assertThatValue { it }

fun <T, R> TestSingleObserver<T>.assertThatValue(mapper: (T) -> R): Assertion<R> {
  this.assertSuccess()
  return assertThat(mapper(this.value))
}

fun <T> TestMaybeObserver<T>.assertThatValue() =
  this.assertThatValue { it }

fun <T, R> TestMaybeObserver<T>.assertThatValue(mapper: (T) -> R): Assertion<R> {
  this.assertSuccess()
  return assertThat(mapper(this.value))
}
