package omnia.util.test.fluent

import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertTrue

fun <T> Assertion<Iterable<T>>.isEmpty(): Assertion<Iterable<T>> {
  assertFalse(actual.iterator().hasNext(), message = message)
  return this
}

fun <T> Assertion<Iterable<T>>.isNotEmpty(): Assertion<Iterable<T>> {
  assertTrue(actual.iterator().hasNext(), message = message)
  return this
}

fun <T> Assertion<Iterable<T>>.contains(expected: T): Assertion<Iterable<T>> {
  assertContains(actual, expected, message)
  return this
}
