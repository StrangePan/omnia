package omnia.contract;

/**
 * A parameterized version of {@link Container} with a type-safe alternative to {@link
 * #containsUnknownTyped(Object)}.
 */
public interface TypedContainer<T> extends Container {

  /**
   * Checks if this {@link Container} contains the given instance. Functionally identical to
   * {@link #containsUnknownTyped(Object)} except that the type of the object is checked at
   * compile time. This version is less error prone and should be preferred over the more
   * error-prone {@link #containsUnknownTyped(Object)}.
   *
   * <p>Instances will be compared using the {@link Object#equals(Object)} method as defined by the
   * contained type. It is important that this type appropriately implement this method
   * according to the contract set forth by the core Java libraries.
   *
   * @param element the element to check for
   * @return {@code true} if the given object (or a functional equivalent as defined by {@link
   *     Object#equals(Object)})
   */
  default boolean contains(T element) {
    return containsUnknownTyped(element);
  }
}
