package omnia.util.test.fluent

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import omnia.contract.Container
import omnia.contract.Countable
import omnia.contract.TypedContainer
import omnia.data.structure.Collection

fun <T : Countable> Assertion<T>.hasCount(expected: Int): Assertion<T> {
  assertEquals(expected, actual.count, message)
  return this
}

fun <T : Countable> Assertion<T>.isPopulated(): Assertion<T> {
  assertTrue(actual.isPopulated, message)
  return this
}

fun <T : Countable> Assertion<T>.isEmpty(): Assertion<T> {
  assertFalse(actual.isPopulated, message)
  return this
}

fun <T, C : TypedContainer<T>> Assertion<C>.contains(vararg expected: T): Assertion<C> {
  return containsElementsIn(expected.asIterable())
}

fun <T, C : TypedContainer<T>> Assertion<C>.containsElementsIn(expected: Iterable<T>):
    Assertion<C> {
  for (item in expected) {
    assertTrue(actual.contains(item), message)
  }
  return this
}

fun <T : Container> Assertion<T>.containsUnknownTyped(vararg expected: Any?): Assertion<T> {
  for (item in expected) {
    assertTrue(actual.containsUnknownTyped(item), message)
  }
  return this
}

fun <T, C : Collection<T>> Assertion<C>.containsExactly(vararg expected: T): Assertion<C> {
  return containsExactlyElementsIn(expected.asIterable())
}

fun <T, C : Collection<T>> Assertion<C>.containsExactlyElementsIn(expected: Iterable<T>):
    Assertion<C> {
  Assertion<C>(this.actual, "collection has incorrect number of elements").hasCount(expected.count())
  return containsElementsIn(expected)
}

fun <T, C : Collection<T>> Assertion<C>.containsExactlyUnknownTyped(vararg expected: Any?):
    Assertion<C> {
  Assertion<C>(this.actual, "collection has incorrect number of elements").hasCount(expected.count())
  return containsUnknownTyped(*expected)
}

fun <T, C : TypedContainer<T>> Assertion<C>.doesNotContain(vararg expected: T): Assertion<C> {
  for (item in expected) {
    assertFalse(actual.contains(item), message)
  }
  return this
}

fun <T, C : Iterable<T>> Assertion<C>.allMatch(matcher: (T) -> Boolean): Assertion<C> {
  for (item in actual) {
    assertTrue(matcher(item), message)
  }
  return this
}
