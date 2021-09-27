package omnia.data.structure.mutable

import java.util.Optional
import java.util.function.Supplier
import java.util.stream.Stream
import omnia.data.iterate.MappingIterator
import omnia.data.stream.Collectors
import omnia.data.structure.Collection
import omnia.data.structure.Map
import omnia.data.structure.Set

open class MaskingMap<K, V>(private val kotlinMap: kotlin.collections.MutableMap<K, V>) :
  MutableMap<K, V> {

  override fun putMapping(key: K, value: V) {
    kotlinMap[key] = value
  }

  override fun putMappingIfAbsent(key: K, value: Supplier<V>): V {
    return kotlinMap.computeIfAbsent(key, { value.get() })
  }

  override fun removeUnknownTypedKey(key: Any?): Optional<V> {
    return Optional.ofNullable(kotlinMap.remove(key))
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

      override fun stream(): Stream<Map.Entry<K, V>> {
        return kotlinMap.entries.stream().map { javaEntry -> Map.Entry.masking(javaEntry) }
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

  override fun valueOfUnknownTyped(key: Any?): Optional<V> {
    return Optional.ofNullable(kotlinMap[key])
  }

  override fun keysOfUnknownTyped(value: Any?): Set<K> {
    return kotlinMap.entries
      .stream()
      .filter { e -> e.value == value }
      .map { e -> e.key }
      .collect(Collectors.toImmutableSet())
  }

  override fun toString(): String {
    return kotlinMap.toString()
  }
}