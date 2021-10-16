package omnia.reference

actual class WeakReference<T : Any> private constructor(value: T? = null) {
  private val weakReference: java.lang.ref.WeakReference<T>

  init {
    weakReference = java.lang.ref.WeakReference(value)
  }

  actual companion object {
    actual fun <T : Any> of(value: T) = WeakReference<T>(value)

    actual fun <T : Any> empty() = WeakReference<T>()
  }

  actual fun clear() = weakReference.clear()

  /** Setter and getter for the held value. To avoid race conditions, always save the value to a
   * local variable before using it. */
  actual val value: T? = weakReference.get()
}