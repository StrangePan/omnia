package omnia.util.test.fluent

import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

fun <T, C : Collection<T>> Assertion<C>.hasCount(expected: Int): Assertion<C> {
  assertEquals(expected, actual.count(), message)
  return this
}

fun <T, C : Collection<T>> Assertion<C>.isNotEmpty(): Assertion<C> {
  assertTrue(actual.isNotEmpty(), message)
  return this
}

fun <T, C : Collection<T>> Assertion<C>.isEmpty(): Assertion<C> {
  assertTrue(actual.isEmpty(), message)
  return this
}

fun <T, C : Collection<T>> Assertion<C>.contains(vararg expected: T): Assertion<C> {
  for (item in expected) {
    assertContains(actual, item, message)
  }
  return this
}

fun <T, C : Collection<T>> Assertion<C>.containsExactly(vararg expected: T): Assertion<C> {
  assertEquals(expected.count(), actual.count(), message)
  return contains(*expected)
}

fun <T, C : Collection<T>> Assertion<C>.doesNotContain(vararg expected: T): Assertion<C> {
  for (item in expected) {
    assertFalse(actual.contains(item), message)
  }
  return this
}
