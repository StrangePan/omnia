package omnia.data.cache

internal class MockSupplier<T> constructor(private val value: T) : () -> T {
  var invocations = 0

  override fun invoke(): T {
    ++invocations
    return value
  }
}