package omnia.data.structure.mutable

import java.util.stream.Collectors
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
      return HashMap(
        original.entries().stream().collect(
          Collectors.toMap(Map.Entry<out K, out V>::key, Map.Entry<out K, out V>::value)
        )
      )
    }
  }
}