package omnia.data.structure.mutable

import omnia.data.structure.Map

class HashMap<K : Any, V : Any> private constructor() :
  MaskingMap<K, V>(kotlin.collections.HashMap()) {

  companion object {

    fun <K : Any, V : Any> create(): HashMap<K, V> {
      return HashMap()
    }

    fun <K : Any, V : Any> copyOf(original: Map<out K, out V>): HashMap<K, V> {
      return original.entries().toHashMap({ it.key() }, { it.value() })
    }

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