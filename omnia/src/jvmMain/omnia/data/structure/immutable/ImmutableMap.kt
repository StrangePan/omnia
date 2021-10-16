package omnia.data.structure.immutable

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.data.cache.MemoizedInt
import omnia.data.structure.Collection
import omnia.data.structure.Map
import omnia.data.structure.Set
import omnia.data.structure.immutable.ImmutableSet.Companion.toImmutableSet
import omnia.data.structure.tuple.Couple

class ImmutableMap<K : Any, V : Any> : Map<K, V> {

  private val backingMap: MutableMap<K, V> = kotlin.collections.HashMap()
  fun toBuilder(): Builder<K, V> {
    return buildUpon(this)
  }

  class Builder<K : Any, V : Any> {

    val backingMap: MutableMap<K, V> = kotlin.collections.HashMap()
    fun putMapping(key: K, value: V): Builder<K, V> {
      backingMap[key] = value
      return this
    }

    fun putMappingIfAbsent(key: K, value: () -> V): Builder<K, V> {
      backingMap.computeIfAbsent(key) { value() }
      return this
    }

    fun putAll(otherMap: Map<out K, out V>): Builder<K, V> {
      otherMap.entries().forEach { putMapping(it.key(), it.value()) }
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
      backingMap.remove(key)
      return this
    }

    fun build(): ImmutableMap<K, V> {
      return if (backingMap.isEmpty()) empty() else ImmutableMap(this)
    }
  }

  private constructor()
  
  private constructor(map: kotlin.collections.Map<K, V>) {
    this.backingMap.putAll(map)
  }

  private constructor(builder: Builder<K, V>) : this(builder.backingMap)

  override fun keys(): Set<K> {
    return Set.masking(backingMap.keys)
  }

  override fun values(): Collection<V> {
    return Collection.masking(backingMap.values)
  }

  override fun entries(): ImmutableSet<Map.Entry<K, V>> {
    class Entry(private val backingEntry: MutableMap.MutableEntry<K, V>) : Map.Entry<K, V> {

      override fun key(): K {
        return backingEntry.key
      }

      override fun value(): V {
        return backingEntry.value
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

      override fun hashCode() = hash(key(), value())

      override fun toString(): String {
        return key().toString() + " => " + value().toString()
      }
    }
    return backingMap.entries.map { Entry(it) }.toImmutableSet()
  }

  override fun valueOfUnknownTyped(key: Any?): V? {
    return backingMap[key]
  }

  override fun keysOfUnknownTyped(value: Any?): ImmutableSet<K> {
    return backingMap.entries.filter { it.value == value }.map { it.key }.toImmutableSet()
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
      val otherValue = other.valueOfUnknownTyped(entry.key())
      if (otherValue != entry.value()) {
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
    entries.forEachIndexed { index, entry -> entryCodes[index] = entry.hashCode() }
    entryCodes.sort()
    return hash(entryCodes.contentHashCode())
  }

  override fun toString(): String {
    return "${this::class.simpleName}[${entries().joinToString { "{$it}" }}]"
  }

  companion object {

    private val EMPTY_IMMUTABLE_MAP: ImmutableMap<*, *> = ImmutableMap<Any, Any>()

    @JvmStatic
    fun <K : Any, V : Any> empty(): ImmutableMap<K, V> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_IMMUTABLE_MAP as ImmutableMap<K, V>
    }

    @JvmStatic
    fun <K : Any, V : Any> of(key: K, value: V): ImmutableMap<K, V> {
      return ImmutableMap(mapOf(Pair(key, value)))
    }

    @JvmStatic
    fun <K : Any, V : Any> copyOf(otherMap: kotlin.collections.Map<out K, V>): ImmutableMap<K, V> {
      return copyOf(Map.masking(otherMap))
    }

    @JvmStatic
    fun <K : Any, V : Any> copyOf(otherMap: Map<out K, out V>): ImmutableMap<K, V> {
      return if (otherMap is ImmutableMap<*, *>) {
        @Suppress("UNCHECKED_CAST")
        otherMap as ImmutableMap<K, V>
      } else builder<K, V>().putAll(otherMap).build()
    }

    @JvmStatic
    fun <K : Any, V : Any> copyOf(iterable: Iterable<Map.Entry<out K, out V>>): ImmutableMap<K, V> {
      return builder<K, V>().putAll(iterable).build()
    }

    @JvmStatic
    fun <K : Any, V : Any, E> Iterable<E>.toImmutableMap(
      keyMapper: (E) -> K,
      valueMapper: (E) -> V
    ): ImmutableMap<K, V> {
      val builder = builder<K, V>()
      this.forEach { builder.putMapping(keyMapper(it), valueMapper(it)) }
      return builder.build()
    }

    @JvmStatic
    fun <K : Any, V : Any> Iterable<Couple<out K, out V>>.toImmutableMap(): ImmutableMap<K, V> {
      return this.toImmutableMap({ it.first() }, { it.second() })
    }

    @JvmStatic
    fun <K : Any, V : Any> builder(): Builder<K, V> {
      return Builder()
    }

    @JvmStatic
    fun <K : Any, V : Any> buildUpon(other: Map<out K, out V>): Builder<K, V> {
      return builder<K, V>().putAll(other)
    }
  }
}