package omnia.data.contract

/**
 * A writable, mutable object that can be converted to a read-only counterpart. Useful for
 * making available read-only views of data structures.
 *
 * @param E the read-only counterpart of this object.
 */
interface Writable<E> {

  /**
   * Creates a read-only view for the current object. Changes to the current object will be
   * reflected in the read-only view, but the read-only view cannot push changes to this object.
   *
   * @return a read-only view of the current object.
   */
  fun toReadOnly(): E
}