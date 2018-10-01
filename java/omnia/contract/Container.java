package omnia.contract;

/**
 * A {@link Container} is an object that contains some other object and can be queried to see if
 * this particular instance contains another specific instance.
 *
 * <p>An example of a {@link Container} is a list of integers, which would also be a container of
 * integers.
 *
 * @param <E> the type contained by this container
 */
public interface Container<E> {

  /**
   * Checks if this {@link Container} contains the given instance.
   *
   * <p>Instances will be compared using the {@link Object#equals(Object)} method as defined by the
   * contained type {@link E}. It is important that this type appropriately implement this method
   * according to the contract set forth by the core Java libraries.
   *
   * @param element the element to check for
   * @return {@code true} if the given object (or a functional equivalent as defined by {@link
   *     Object#equals(Object)})
   */
  boolean contains(E element);
}
