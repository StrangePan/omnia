package omnia.util.test.fluent

import kotlin.test.assertFalse
import kotlin.test.assertTrue

fun Assertion<String>.isEmpty(): Assertion<String> {
  assertTrue(actual.isEmpty(), message)
  return this
}

fun Assertion<String>.isNotEmpty(): Assertion<String> {
  assertTrue(actual.isNotEmpty(), message)
  return this
}

fun Assertion<String>.startsWith(expected: String): Assertion<String> {
  assertTrue(actual.startsWith(expected), message)
  return this
}

fun Assertion<String>.doesNotStartWith(expected: String): Assertion<String> {
  assertFalse(actual.startsWith(expected), message)
  return this

}

fun Assertion<String>.endsWith(expected: String): Assertion<String> {
  assertTrue(actual.endsWith(expected), message)
  return this
}

fun Assertion<String>.doesNotEndWith(expected: String): Assertion<String> {
  assertFalse(actual.endsWith(expected), message)
  return this
}

fun Assertion<String>.contains(expected: String): Assertion<String> {
  assertTrue(
      actual.contains(expected),
      message ?: """string does not contain expected substring
        |  wanted: $expected
        |  actual: $actual""".trimMargin())
  return this
}

fun Assertion<String>.doesNotContain(expected: String): Assertion<String> {
  assertFalse(
      actual.contains(expected),
      message ?: """string contains unexpected substring
        |  unwated: $expected
        |  actual: $actual""".trimMargin())
  return this
}

fun Assertion<String>.matches(expected: String): Assertion<String> {
  return matches(Regex(expected))
}

fun Assertion<String>.matches(expected: Regex): Assertion<String> {
  assertTrue(actual.matches(expected), message)
  return this
}

fun Assertion<String>.doesNotMatch(expected: String): Assertion<String> {
  return doesNotMatch(Regex(expected))
}

fun Assertion<String>.doesNotMatch(expected: Regex): Assertion<String> {
  assertFalse(actual.matches(expected), message)
  return this
}

fun Assertion<String>.containsMatch(expected: String): Assertion<String> {
  return containsMatch(Regex(expected))
}

fun Assertion<String>.containsMatch(expected: Regex): Assertion<String> {
  assertTrue(actual.contains(expected), message)
  return this
}

fun Assertion<String>.doesNotContainMatch(expected: String): Assertion<String> {
  return doesNotContainMatch(Regex(expected))
}

fun Assertion<String>.doesNotContainMatch(expected: Regex): Assertion<String> {
  assertFalse(actual.contains(expected), message)
  return this
}
