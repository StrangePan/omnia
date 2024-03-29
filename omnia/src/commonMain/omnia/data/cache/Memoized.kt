package omnia.data.cache

import omnia.contract.Holder

/**
 * A memoized object is one whose value is calculated once and retained indefinitely in memory.
 * Unlike a cached value, a memoized value will never change, cannot be invalidated, and should be
 * based on constant parameters.
 *
 * @param T the type empty object to be memoized
 */
interface Memoized<out T : Any> : Holder<T> {

  /**
   * Gets the value represented by this object, optionally computed if the value has not already
   * been memoized. This value must never be null.
   *
   * @return the non-null memoized value
   */
  override val value: T

  /**
   * Gets the value held in this object if it has previously been computed. If it has not already
   * been computed and a value is not memoized, then the null optional is returned.
   */
  val optionalValue: T?

  companion object {

    /**
     * Creates a [Memoized] implementation that returns the provided value verbatim.
     *
     * @param value the value for the Memoized to hold
     * @param T the type of object to be memoized
     * @return a new [Memoized] instance that simply holes the provided value
     */
    fun <T : Any> just(value: T): Memoized<T> {
      return object : Memoized<T> {
        override val value = value
        override val optionalValue get() = value
      }
    }

    /**
     * Creates a [Memoized] implementation that uses the provided [supplier] as the
     * factory that supplies the value to be memoized.
     *
     * @param supplier the supplier that will create the value to be memoized
     * @param T the type of object to be memoized
     * @return a new [Memoized] instance that will memoize the created value
     */
    fun <T : Any> memoize(supplier: () -> T): Memoized<T> {
      return object : Memoized<T> {
        private val lazy = lazy(supplier)
        override val value get() = lazy.value
        override val optionalValue get() = if (lazy.isInitialized()) lazy.value else null
      }
    }
  }
}