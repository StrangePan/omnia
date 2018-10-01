package omnia.contract;

/**
 * A {@link IntHolder} is an object that acts as a vessel for a single {@code int} value. Holders
 * usually provide additional functionality that a raw int variable cannot provide. Examples include
 * caching, weak references, etc.
 *
 * @see Holder for a more general-purpose object container
 */
public interface IntHolder {

  /**
   * Gets the value held in this object.
   *
   * @return the number held in this object, which may be any valid {@code int} value.
   */
  int value();
}
