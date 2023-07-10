package omnia.util.test.fluent

import kotlin.reflect.KClass
import kotlin.test.assertFailsWith
import omnia.util.test.fluent.Assertion.Companion.assertThat

fun <E : Throwable> Assertion<()->Unit>.failsWith(throwableClass: KClass<E>): Assertion<E> =
  assertThat(assertFailsWith(throwableClass, this.message, this.actual))

fun Assertion<()->Unit>.fails(): Assertion<Throwable> = this.failsWith(Throwable::class)

fun assertThatCode(code: ()->Unit) = assertThat(code)
