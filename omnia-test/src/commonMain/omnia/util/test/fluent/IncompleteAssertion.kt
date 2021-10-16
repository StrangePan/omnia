package omnia.util.test.fluent

class IncompleteAssertion constructor(private val message: String) {
  fun <T> that(actual: T) = Assertion(actual, message)

  companion object {
    fun assertWithMessage(message: String) = IncompleteAssertion(message)
  }
}