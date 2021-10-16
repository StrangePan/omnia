package omnia.contract

/**
 * A parameterized version of [Container] with a type-safe alternative to [containsUnknownTyped].
 */
interface TypedContainer<T : Any> : Container {

  /**
   * Checks if this [Container] contains the given instance. Functionally identical to
   * [containsUnknownTyped] except that the type of the object is checked at compile time. This
   * version is less error-prone and should be preferred over the more error-prone
   * [containsUnknownTyped].
   *
   * Instances will be compared using the [Object.equals] method as defined by the
   * contained type. It is important that this type appropriately implement this method
   * according to the contract set forth by the core platform libraries.
   *
   * @param item the element to check for
   * @return `true` if the given object (or a functional equivalent as defined by [Object.equals])
   */
  operator fun contains(item: T): Boolean {
    return containsUnknownTyped(item)
  }
}