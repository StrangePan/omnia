package omnia.data.structure.immutable

import java.util.Arrays
import java.util.Collections
import java.util.Objects
import java.util.Optional
import java.util.function.Supplier
import omnia.data.cache.MemoizedInt
import omnia.data.stream.Collectors
import omnia.data.structure.Collection
import omnia.data.structure.Map
import omnia.data.structure.Set

class ImmutableMap<K, V> : Map<K, V> {

  private val javaMap: MutableMap<K, V> = kotlin.collections.HashMap()
  fun toBuilder(): Builder<K, V> {
    return buildUpon(this)
  }

  class Builder<K, V> {

    val javaMap: MutableMap<K, V> = kotlin.collections.HashMap()
    fun putMapping(key: K, value: V): Builder<K, V> {
      javaMap[key] = value
      return this
    }

    fun putMappingIfAbsent(key: K, value: Supplier<out V>): Builder<K, V> {
      Objects.requireNonNull(value)
      javaMap.computeIfAbsent(Objects.requireNonNull(key), { value.get() })
      return this
    }

    fun putAll(otherMap: Map<out K, out V>): Builder<K, V> {
      otherMap.entries().stream().forEach { e -> putMapping(e.key(), e.value()) }
      return this
    }

    fun putAll(iterable: Iterable<Map.Entry<out K, out V>>): Builder<K, V> {
      iterable.forEach { e: Map.Entry<out K, out V> -> putMapping(e.key(), e.value()) }
      return this
    }

    fun removeKey(key: K): Builder<K, V> {
      return removeUnknownTypedKey(key)
    }

    fun removeUnknownTypedKey(key: Any?): Builder<K, V> {
      javaMap.remove(key)
      return this
    }

    fun build(): ImmutableMap<K, V> {
      return if (javaMap.isEmpty()) empty() else ImmutableMap(this)
    }
  }

  private constructor()
  private constructor(javaMap: MutableMap<K, V>) {
    this.javaMap.putAll(javaMap)
  }

  private constructor(builder: Builder<K, V>) : this(builder.javaMap)

  override fun keys(): Set<K> {
    return Set.masking(javaMap.keys)
  }

  override fun values(): Collection<V> {
    return Collection.masking(javaMap.values)
  }

  override fun entries(): Set<Map.Entry<K, V>> {
    class Entry(private val javaEntry: MutableMap.MutableEntry<K, V>) : Map.Entry<K, V> {

      override fun key(): K {
        return javaEntry.key
      }

      override fun value(): V {
        return javaEntry.value
      }

      override fun equals(other: Any?): Boolean {
        if (other === this) {
          return true
        }
        if (other !is Map.Entry<*, *>) {
          return false
        }
        return (key() == other.key()
            && value() == other.value())
      }

      override fun hashCode(): Int {
        return Objects.hash(key(), value())
      }

      override fun toString(): String {
        return key().toString() + " => " + value().toString()
      }
    }
    return javaMap.entries.stream()
      .map { javaEntry -> Entry(javaEntry) }
      .collect(Collectors.toImmutableSet())
  }

  override fun valueOfUnknownTyped(key: Any?): Optional<V> {
    return Optional.ofNullable(javaMap.get(key))
  }

  override fun keysOfUnknownTyped(value: Any?): Set<K> {
    return javaMap.entries
      .stream()
      .filter { e -> e.value == value }
      .map { entry -> entry.key }
      .collect(Collectors.toImmutableSet())
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) {
      return true
    }
    if (other !is ImmutableMap<*, *>) {
      return false
    }
    if (other.entries().count() != entries().count()) {
      return false
    }
    for (entry in entries()) {
      val otherValue: Optional<*> = other.valueOfUnknownTyped(entry.key())
      if (otherValue.isEmpty || otherValue.get() != entry.value()) {
        return false
      }
    }
    return true
  }

  override fun hashCode(): Int {
    return hashCode.value()
  }

  private val hashCode: MemoizedInt = MemoizedInt.memoize { computeHash() }
  private fun computeHash(): Int {
    val entries: Set<out Map.Entry<*, *>> = entries()
    val entryCodes = IntArray(entries.count())
    var i = 0
    for (entry in entries) {
      entryCodes[i++] = entry.hashCode()
    }
    Arrays.sort(entryCodes)
    return Objects.hash(entryCodes.contentHashCode())
  }

  override fun toString(): String {
    return (javaClass.simpleName
        + "["
        + entries().stream().map { obj: Map.Entry<K, V> -> obj.toString() }
      .map { s: String -> "{$s}" }.collect(java.util.stream.Collectors.joining(", "))
        + "]")
  }

  companion object {

    private val EMPTY_IMMUTABLE_MAP: ImmutableMap<*, *> = ImmutableMap<Any, Any>()
    fun <K, V> empty(): ImmutableMap<K, V> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_IMMUTABLE_MAP as ImmutableMap<K, V>
    }

    fun <K, V> of(key: K, value: V): ImmutableMap<K, V> {
      return ImmutableMap(Collections.singletonMap(key, value))
    }

    fun <K, V> copyOf(otherMap: kotlin.collections.Map<out K, V>): ImmutableMap<K, V> {
      return copyOf(Map.masking(otherMap))
    }

    fun <K, V> copyOf(otherMap: Map<out K, out V>): ImmutableMap<K, V> {
      return if (otherMap is ImmutableMap<*, *>) {
        @Suppress("UNCHECKED_CAST")
        otherMap as ImmutableMap<K, V>
      } else builder<K, V>().putAll(otherMap).build()
    }

    fun <K, V> copyOf(iterable: Iterable<Map.Entry<out K, out V>>): ImmutableMap<K, V> {
      return builder<K, V>().putAll(iterable).build()
    }

    fun <K, V> builder(): Builder<K, V> {
      return Builder()
    }

    fun <K, V> buildUpon(other: Map<out K, out V>): Builder<K, V> {
      return builder<K, V>().putAll(other)
    }
  }
}