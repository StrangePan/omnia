package omnia.util.test.fluent

class Assertion<T> constructor(val actual: T, val message: String? = null) {
  companion object {
    fun <T> assertThat(actual: T) = Assertion(actual)
  }
}
