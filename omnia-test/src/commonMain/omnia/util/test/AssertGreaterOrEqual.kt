package omnia.util.test

import kotlin.test.assertTrue

fun <T : Comparable<T>> assertGreaterOrEqual(expected: T, actual: T, message: String? = null) {
  assertTrue(
    expected <= actual,
    (if (message != null) "$message. " else "") + "Expected $expected, actual $actual.")
}