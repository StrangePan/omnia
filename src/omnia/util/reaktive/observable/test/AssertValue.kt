package omnia.util.reaktive.observable.test

import com.badoo.reaktive.test.observable.TestObservableObserver
import kotlin.test.assertTrue
import omnia.util.test.assertGreaterOrEqual

fun <T> TestObservableObserver<T>.assertValue(predicate: (T) -> Boolean): TestObservableObserver<T> {
  return this.assertValue(0, predicate)
}

fun <T> TestObservableObserver<T>.assertValue(index: Int, predicate: (T) -> Boolean):
    TestObservableObserver<T> {
  assertGreaterOrEqual(0, index)
  assertGreaterOrEqual(index, this.values.size)
  assertTrue(predicate(this.values[index]))
  return this
}