package omnia.util.test.fluent

import kotlin.test.assertFalse

fun <T> Assertion<Iterable<T>>.isEmpty(): Assertion<Iterable<T>> {
  assertFalse(actual.iterator().hasNext(), message = message)
  return this
}
