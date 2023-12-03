package omnia.reference

/**
 * A wrapper around a value that holds a weak reference to the value. That is, the value could be
 * deallocated and this wrapper cleared at any time.
 */
expect class WeakReference<T : Any> {

  companion object {
    fun <T : Any> of(value: T): WeakReference<T>

    fun <T : Any> empty(): WeakReference<T>
  }

  fun clear()

  /** Setter and getter for the held value. To avoid race conditions, always save the value to a
   * local variable before using it. */
  val value: T?
}
