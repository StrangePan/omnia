package omnia.data.structure.mutable

import kotlin.collections.MutableMap as KotlinMutableList
import omnia.data.iterate.MappingIterator
import omnia.data.structure.Collection
import omnia.data.structure.Map
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet

open class MaskingMap<K : Any, V : Any>(
  private val backingMap: KotlinMutableList<K, V>
)
  : MutableMap<K, V> {

  override fun putMapping(key: K, value: V) {
    backingMap[key] = value
  }

  override fun putMappingIfAbsent(key: K, value: () -> V): V {
    return backingMap.getOrPut(key) { value() }
  }

  override fun removeUnknownTypedKey(key: Any?): V? {
    return backingMap.remove(key)
  }

  override fun keys(): Set<K> {
    return Set.masking(backingMap.keys)
  }

  override fun values(): Collection<V> {
    return Collection.masking(backingMap.values)
  }

  override fun entries(): Set<Map.Entry<K, V>> {
    return object : Set<Map.Entry<K, V>> {
      override fun containsUnknownTyped(item: Any?): Boolean {
        return (item is Map.Entry<*, *> && backingMap[item.key()] == item.value())
      }

      override fun count(): Int {
        return backingMap.size
      }

      override fun iterator(): Iterator<Map.Entry<K, V>> {
        return MappingIterator(backingMap.entries.iterator()) { Map.Entry.masking(it) }
      }
    }
  }

  override fun valueOfUnknownTyped(key: Any?): V? {
    return backingMap[key]
  }

  override fun keysOfUnknownTyped(value: Any?): Set<K> {
    return backingMap.entries.filter { it.value == value }.map { it.key }.toImmutableSet()
  }

  override fun toString(): String {
    return backingMap.toString()
  }
}