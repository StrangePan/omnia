package omnia.contract

/**
 * A [Holder] is an object that acts as a vessel for a single other value. Holders usually
 * provide additional functionality that a raw reference cannot provide. Examples include caching,
 * weak references, etc.
 *
 * @param T the type empty object held by this holder
 * @see IntHolder for an {@code int} primitive holder
 *
 * @see DoubleHolder for a {@code double} primitive holder
</T> */
interface Holder<T> {

  /**
   * Gets the value held in this object. The returned reference will never be null.
   *
   * @return a non-null reference held by this class
   */
  fun value(): T
}