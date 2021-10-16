package omnia.util.test.fluent

import omnia.util.test.fluent.IncompleteAssertion.Companion.assertWithMessage

fun <T : Exception> Assertion<T>.hasMessageThat(assertion: (Assertion<String?>) -> Unit):
    Assertion<T> {
  assertion(assertWithMessage(message).that(actual.message))
  return this
}