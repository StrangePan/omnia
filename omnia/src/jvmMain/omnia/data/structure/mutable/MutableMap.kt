package omnia.data.structure.mutable

import omnia.data.structure.Map
import omnia.data.structure.Set

interface MutableMap<K : Any, V : Any> : Map<K, V> {

  fun putMapping(key: K, value: V)

  fun putMappingIfAbsent(key: K, value: () -> V): V

  fun removeKey(key: K): V? {
    return removeUnknownTypedKey(key)
  }

  fun removeUnknownTypedKey(key: Any?): V?

  override fun entries(): Set<Map.Entry<K, V>>

  companion object {

    fun <K : Any, V : Any> masking(backingMap: kotlin.collections.MutableMap<K, V>): MutableMap<K, V> {
      return MaskingMap(backingMap)
    }
  }
}