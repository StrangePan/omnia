package omnia.data.structure.mutable

import omnia.data.iterate.MappingIterator
import omnia.data.stream.Collectors
import omnia.data.structure.Collection
import omnia.data.structure.Map
import omnia.data.structure.Set
import java.util.Optional
import java.util.function.Supplier
import java.util.stream.Stream

open class MaskingMap<K, V>(private val javaMap: kotlin.collections.MutableMap<K, V>) : MutableMap<K, V> {
    override fun putMapping(key: K, value: V) {
        javaMap[key] = value
    }

    override fun putMappingIfAbsent(key: K, value: Supplier<V>): V {
        return javaMap.computeIfAbsent(key, { value.get() })
    }

    override fun removeUnknownTypedKey(key: Any?): Optional<V> {
        return Optional.ofNullable(javaMap.remove(key))
    }

    override fun keys(): Set<K> {
        return Set.masking(javaMap.keys)
    }

    override fun values(): Collection<V> {
        return Collection.masking(javaMap.values)
    }

    override fun entries(): Set<Map.Entry<K, V>> {
        return object : Set<Map.Entry<K, V>> {
            override fun containsUnknownTyped(item: Any?): Boolean {
                return (item is Map.Entry<*, *> && javaMap[item.key()] == item.value())
            }

            override fun stream(): Stream<Map.Entry<K, V>> {
                return javaMap.entries.stream().map { javaEntry -> Map.Entry.masking(javaEntry) }
            }

            override fun count(): Int {
                return javaMap.size
            }

            override fun iterator(): Iterator<Map.Entry<K, V>> {
                return MappingIterator(javaMap.entries.iterator()) { javaEntry -> Map.Entry.masking(javaEntry) }
            }
        }
    }

    override fun valueOfUnknownTyped(key: Any?): Optional<V> {
        return Optional.ofNullable(javaMap[key])
    }

    override fun keysOfUnknownTyped(value: Any?): Set<K> {
        return javaMap.entries
            .stream()
            .filter{ e -> e.value == value }
            .map{ e -> e.key }
            .collect(Collectors.toImmutableSet())
    }

    override fun toString(): String {
        return javaMap.toString()
    }
}