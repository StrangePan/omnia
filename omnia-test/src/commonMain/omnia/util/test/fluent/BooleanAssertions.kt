package omnia.util.test.fluent

import kotlin.test.assertFalse
import kotlin.test.assertTrue

fun Assertion<Boolean>.isTrue(): Assertion<Boolean> {
  assertTrue(actual, message)
  return this
}

fun Assertion<Boolean>.isFalse(): Assertion<Boolean> {
  assertFalse(actual, message)
  return this
}