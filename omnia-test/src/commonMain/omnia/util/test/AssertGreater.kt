package omnia.util.test

import kotlin.test.assertTrue

fun <T : Comparable<T>> assertGreater(expected: T, actual: T, message: String? = null) {
  assertTrue(
      actual > expected,
      (if (message != null) "$message. " else "") + "Expected $actual > $expected")
}