package omnia.contract

/**
 * A [DoubleHolder] is an object that acts as a vessel for a single `double` primitive
 * value. DoubleHolders usually provide additional functionality that a raw double primitive
 * variable cannot provide. Examples include caching, weak references, etc.
 *
 * @see Holder for a more general-purpose object container
 */
interface DoubleHolder {

  /**
   * Gets the value held in this object.
   *
   * @return the number held in this object, which may be any valid `double` value.
   */
  fun value(): Double
}