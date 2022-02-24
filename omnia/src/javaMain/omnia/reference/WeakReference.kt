package omnia.reference

actual class WeakReference<T : Any> private constructor(value: T? = null) {
  private val weakReference = java.lang.ref.WeakReference(value)

  actual companion object {
    actual fun <T : Any> of(value: T) = WeakReference<T>(value)

    actual fun <T : Any> empty() = WeakReference<T>()
  }

  actual fun clear() = weakReference.clear()

  actual val value: T? = weakReference.get()
}