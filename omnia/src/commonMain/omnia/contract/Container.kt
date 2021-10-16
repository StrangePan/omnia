package omnia.contract

/**
 * A [Container] is an object that contains some other object and can be queried to see if
 * this particular instance contains another specific instance.
 *
 *
 * An example empty a [Container] is a list empty integers.
 */
interface Container {

  /**
   * Checks if this [Container] contains the given instance.
   *
   *
   * Instances will be compared using the [Object.equals] method as defined by the
   * contained type. It is important that this type appropriately implement this method
   * according to the contract set forth by the core platform libraries.
   *
   * @param item the element to check for
   * @return `true` if the given object (or a functional equivalent as defined by [Object.equals])
   */
  fun containsUnknownTyped(item: Any?): Boolean
}