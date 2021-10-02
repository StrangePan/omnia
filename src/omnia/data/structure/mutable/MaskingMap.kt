package omnia.data.structure.mutable

import java.util.function.Supplier
import omnia.data.iterate.MappingIterator
import omnia.data.structure.Collection
import omnia.data.structure.Map
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet

open class MaskingMap<K : Any, V : Any>(private val kotlinMap: kotlin.collections.MutableMap<K, V>) :
  MutableMap<K, V> {

  override fun putMapping(key: K, value: V) {
    kotlinMap[key] = value
  }

  override fun putMappingIfAbsent(key: K, value: Supplier<V>): V {
    return kotlinMap.computeIfAbsent(key) { value.get() }
  }

  override fun removeUnknownTypedKey(key: Any?): V? {
    return kotlinMap.remove(key)
  }

  override fun keys(): Set<K> {
    return Set.masking(kotlinMap.keys)
  }

  override fun values(): Collection<V> {
    return Collection.masking(kotlinMap.values)
  }

  override fun entries(): Set<Map.Entry<K, V>> {
    return object : Set<Map.Entry<K, V>> {
      override fun containsUnknownTyped(item: Any?): Boolean {
        return (item is Map.Entry<*, *> && kotlinMap[item.key()] == item.value())
      }

      override fun count(): Int {
        return kotlinMap.size
      }

      override fun iterator(): Iterator<Map.Entry<K, V>> {
        return MappingIterator(kotlinMap.entries.iterator()) { javaEntry ->
          Map.Entry.masking(
            javaEntry
          )
        }
      }
    }
  }

  override fun valueOfUnknownTyped(key: Any?): V? {
    return kotlinMap[key]
  }

  override fun keysOfUnknownTyped(value: Any?): Set<K> {
    return kotlinMap.entries.filter { it.value == value }.map { it.key }.toImmutableSet()
  }

  override fun toString(): String {
    return kotlinMap.toString()
  }
}