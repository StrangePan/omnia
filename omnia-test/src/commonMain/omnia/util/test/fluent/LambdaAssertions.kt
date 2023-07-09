package omnia.util.test.fluent

import kotlin.reflect.KClass
import kotlin.test.assertFailsWith
import omnia.util.test.fluent.Assertion.Companion.assertThat

fun <E : Exception> Assertion<()->Unit>.failsWith(exceptionClass: KClass<E>): Assertion<E> =
  assertThat(assertFailsWith(exceptionClass, this.message, this.actual))
