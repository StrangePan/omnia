package omnia.reaktive.observable.test

import com.badoo.reaktive.test.observable.TestObservableObserver
import kotlin.test.assertEquals

fun <T> TestObservableObserver<T>.assertValueCount(expected: Int): TestObservableObserver<T> {
  val actual = this.values.size
  assertEquals(expected, actual,
    "Number of values ${if (actual < expected) "less" else "greater"} than expected")
  return this
}