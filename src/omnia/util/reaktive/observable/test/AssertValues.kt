package omnia.util.reaktive.observable.test

import com.badoo.reaktive.test.observable.TestObservableObserver
import kotlin.test.fail

fun <T> TestObservableObserver<T>.assertValues(predicate: (T) -> Boolean):
    TestObservableObserver<T> {

  val testStatus = this.values.map(predicate)
  val anyFailure = testStatus.any { !it }

  if (anyFailure) {
    val stringifiedValues = (0..testStatus.size).map { i ->
      val stringifiedValue = try { this.values[i] } catch (_: Throwable) { "error stringifying" }
      "  ${if (testStatus[i]) "pass" else "fail"} [$i]: $stringifiedValue"
    }

    // index of values that failed to match
    // stringification of values
    fail(
      "${testStatus.count { !it }} values failed to match. values:\n" +
          stringifiedValues.joinToString("\n")
    )
  }

  return this
}