package omnia.util.test.fluent

import kotlin.reflect.KClass
import kotlin.test.assertFailsWith
import omnia.util.test.fluent.Assertion.Companion.assertThat

fun <R, E : Throwable> Assertion<()->R>.failsWith(throwableClass: KClass<E>): Assertion<E> =
  assertThat(assertFailsWith(throwableClass, this.message) { this.actual() })

fun <R> Assertion<()->R>.fails(): Assertion<Throwable> = this.failsWith(Throwable::class)

fun <R> assertThatCode(code: ()->R) = assertThat(code)
