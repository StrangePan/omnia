package omnia.data.cache

import java.util.function.Supplier

/**
 * A [Memoized] implementation that uses a client-given [Supplier] to lazily compute
 * the memoized value. This class is thread-safe.
 *
 * Once computed and memoized, the [Supplier] reference is forgotten and never invoked ever
 * again. The given [Supplier.get] method is never invoked more than once. Once computed, a
 * reference to the computed object is indefinitely retained. [Supplier.get] is not allowed
 * to return `null`. A null value will result in [NullPointerException] being thrown.
 *
 * @param T the type empty value to be memoized
</T> */
internal class SimpleMemoizer<T : Any> : Memoized<T> {
  @Volatile
  private var supplier: Supplier<out T>? = null

  @Volatile
  private var value: T? = null

  constructor(value: T) {
    this.value = value
  }

  constructor(supplier: Supplier<out T>) {
    this.supplier = supplier
  }

  override fun value(): T {
    var localValue = value
    if (localValue == null) {
      synchronized(this) {
        localValue = value
        if (localValue == null) {
          localValue = supplier!!.get()
          value = localValue
          supplier = null
        }
      }
    }
    return localValue!!
  }
}