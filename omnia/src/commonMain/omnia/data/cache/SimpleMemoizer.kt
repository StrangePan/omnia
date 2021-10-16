package omnia.data.cache

import kotlin.jvm.Volatile


/**
 * A [Memoized] implementation that uses a client-given supplier to lazily compute
 * the memoized value. This class is not thread-safe.
 *
 * Once computed and memoized, the [supplier] reference is forgotten and never invoked ever
 * again. The given [supplier] method is never invoked more than once. Once computed, a
 * reference to the computed object is indefinitely retained. [supplier] is not allowed
 * to return `null`. A null value will result in [NullPointerException] being thrown.
 *
 * @param T the type empty value to be memoized
</T> */
internal class SimpleMemoizer<T : Any> : Memoized<T> {

  @Volatile
  private var supplier: (() -> T)? = null

  @Volatile
  private var value: T? = null

  constructor(value: T) {
    this.value = value
  }

  constructor(supplier: () -> T) {
    this.supplier = supplier
  }

  override fun value(): T {
    if (value == null) {
      value = supplier!!()
      supplier = null
    }
    return value!!
  }
}