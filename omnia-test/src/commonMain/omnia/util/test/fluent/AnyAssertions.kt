package omnia.util.test.fluent

import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import omnia.data.structure.immutable.ImmutableList
import omnia.util.test.fluent.Assertion.Companion.assertThat
import omnia.util.test.fluent.IncompleteAssertion.Companion.assertWithMessage

fun <T> Assertion<T>.isEqualTo(expected: Any?): Assertion<T> {
  assertEquals(expected, actual, message)
  return this
}

fun <T> Assertion<T>.isNotEqualTo(expected: Any?): Assertion<T> {
  assertNotEquals(expected, actual, message)
  return this
}

fun <T> Assertion<T>.isEqualToAnyOf(vararg expected: Any): Assertion<T> {
  assertWithMessage(message).that(ImmutableList.copyOf(expected)).containsUnknownTyped(actual)
  return this
}

fun <T> Assertion<T?>.isNotNull(): Assertion<T> {
  assertNotNull(actual, message)
  return assertWithMessage(message).that(actual)
}

fun <T> Assertion<T>.isNull(): Assertion<T> {
  assertNull(actual, message)
  return this
}

fun <T> Assertion<T>.isSameAs(expected: Any?): Assertion<T> {
  assertSame(expected, actual, message)
  return this
}

fun <T> Assertion<T>.isNotSameAs(expected: Any?): Assertion<T> {
  assertNotSame(expected, actual, message)
  return this
}

fun <T, R: Any> Assertion<T>.isA(expected: KClass<R>): Assertion<R> {
  @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
  assertTrue(
      expected.isInstance(actual),
      message?: "$actual (${if (actual != null) actual!!::class.qualifiedName else "null"}) is not an instance of ${expected.qualifiedName}")
  return assertThat(expected.cast(actual))
}

fun <T> Assertion<T>.isNotA(expected: KClass<*>): Assertion<T> {
  @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
  assertFalse(
      expected.isInstance(actual),
      message?: "$actual (${if (actual != null) actual!!::class.qualifiedName else "null"}) is an instance of ${expected.qualifiedName}")
  return this
}

fun <T> Assertion<T>.isOneOf(expected1: KClass<*>, vararg expectedN: KClass<*>): Assertion<T> {
  val expected = listOf(expected1) + expectedN
  @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
  assertTrue(
    expected.any { it.isInstance(actual) },
    message ?: "$actual (${if (actual != null) actual!!::class.qualifiedName else "null"}) is not an instance of any of: ${expected.joinToString()}"
  )
  return this
}

fun <T, R> Assertion<T>.andThat(mapper: (T) -> R): Assertion<R> {
  return assertThat(mapper(this.actual))
}

fun <T, R> Assertion<T>.andThat(mapper: (T) -> R, asserter: (Assertion<R>) -> Unit): Assertion<T> {
  asserter(this.andThat(mapper))
  return this
}
