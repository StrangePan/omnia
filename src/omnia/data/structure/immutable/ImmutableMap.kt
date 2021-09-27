package omnia.data.structure.immutable

import java.util.Arrays
import java.util.Objects
import java.util.Optional
import java.util.function.Supplier
import omnia.data.cache.MemoizedInt
import omnia.data.stream.Collectors
import omnia.data.structure.Collection
import omnia.data.structure.Map
import omnia.data.structure.Set

class ImmutableMap<K, V> : Map<K, V> {

  private val kotlinMap: MutableMap<K, V> = kotlin.collections.HashMap()
  fun toBuilder(): Builder<K, V> {
    return buildUpon(this)
  }

  class Builder<K, V> {

    val kotlinMap: MutableMap<K, V> = kotlin.collections.HashMap()
    fun putMapping(key: K, value: V): Builder<K, V> {
      kotlinMap[key] = value
      return this
    }

    fun putMappingIfAbsent(key: K, value: Supplier<out V>): Builder<K, V> {
      Objects.requireNonNull(value)
      kotlinMap.computeIfAbsent(Objects.requireNonNull(key)) { value.get() }
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
      kotlinMap.remove(key)
      return this
    }

    fun build(): ImmutableMap<K, V> {
      return if (kotlinMap.isEmpty()) empty() else ImmutableMap(this)
    }
  }

  private constructor()
  private constructor(map: kotlin.collections.Map<K, V>) {
    this.kotlinMap.putAll(map)
  }

  private constructor(builder: Builder<K, V>) : this(builder.kotlinMap)

  override fun keys(): Set<K> {
    return Set.masking(kotlinMap.keys)
  }

  override fun values(): Collection<V> {
    return Collection.masking(kotlinMap.values)
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
    return kotlinMap.entries.stream()
      .map { javaEntry -> Entry(javaEntry) }
      .collect(Collectors.toImmutableSet())
  }

  override fun valueOfUnknownTyped(key: Any?): Optional<V> {
    return Optional.ofNullable(kotlinMap[key])
  }

  override fun keysOfUnknownTyped(value: Any?): Set<K> {
    return kotlinMap.entries
      .stream()
      .filter { it.value == value }
      .map { it.key }
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

    @JvmStatic
    fun <K, V> empty(): ImmutableMap<K, V> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_IMMUTABLE_MAP as ImmutableMap<K, V>
    }

    @JvmStatic
    fun <K, V> of(key: K, value: V): ImmutableMap<K, V> {
      return ImmutableMap(mapOf(Pair(key, value)))
    }

    @JvmStatic
    fun <K, V> copyOf(otherMap: kotlin.collections.Map<out K, V>): ImmutableMap<K, V> {
      return copyOf(Map.masking(otherMap))
    }

    @JvmStatic
    fun <K, V> copyOf(otherMap: Map<out K, out V>): ImmutableMap<K, V> {
      return if (otherMap is ImmutableMap<*, *>) {
        @Suppress("UNCHECKED_CAST")
        otherMap as ImmutableMap<K, V>
      } else builder<K, V>().putAll(otherMap).build()
    }

    @JvmStatic
    fun <K, V> copyOf(iterable: Iterable<Map.Entry<out K, out V>>): ImmutableMap<K, V> {
      return builder<K, V>().putAll(iterable).build()
    }

    @JvmStatic
    fun <K, V> builder(): Builder<K, V> {
      return Builder()
    }

    @JvmStatic
    fun <K, V> buildUpon(other: Map<out K, out V>): Builder<K, V> {
      return builder<K, V>().putAll(other)
    }
  }
}