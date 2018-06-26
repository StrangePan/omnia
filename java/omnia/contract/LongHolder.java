package omnia.contract;

/**
 * A {@link LongHolder} is an object that acts as a vessel for a single {@code double} primitive
 * value. LongHolders usually provide additional functionality that a raw long primitive variable
 * cannot provide. Examples include caching, weak references, etc.
 *
 * @see Holder for a more general-purpose object container
 */
public interface LongHolder {

  /**
   * Gets the value held in this object.
   *
   * @return the number held in this object, which may be any valid {@code long} value.
   */
  long value();
}
