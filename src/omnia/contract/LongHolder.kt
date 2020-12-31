package omnia.contract

/**
 * A [LongHolder] is an object that acts as a vessel for a single `double` primitive
 * value. LongHolders usually provide additional functionality that a raw long primitive variable
 * cannot provide. Examples include caching, weak references, etc.
 *
 * @see Holder for a more general-purpose object container
 */
interface LongHolder {

  /**
   * Gets the value held in this object.
   *
   * @return the number held in this object, which may be any valid `long` value.
   */
  fun value(): Long
}