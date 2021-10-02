package omnia.data.structure.mutable

import omnia.data.structure.Map

class HashMap<K : Any, V : Any> : MaskingMap<K, V> {
  private constructor() : super(java.util.HashMap<K, V>())
  private constructor(original: kotlin.collections.Map<K, V>)
      : super(java.util.HashMap<K, V>(original))

  companion object {

    @JvmStatic
    fun <K : Any, V : Any> create(): HashMap<K, V> {
      return HashMap()
    }

    @JvmStatic
    fun <K : Any, V : Any> copyOf(original: Map<out K, out V>): HashMap<K, V> {
      return original.entries().toHashMap({ it.key() }, { it.value() })
    }

    @JvmStatic
    fun <K : Any, V : Any, E> Iterable<E>.toHashMap(
      keyMapper: (E) -> K,
      valueMapper: (E) -> V
    ): HashMap<K, V> {
      val map = create<K, V>()
      this.forEach { map.putMapping(keyMapper(it), valueMapper(it)) }
      return map
    }
  }
}