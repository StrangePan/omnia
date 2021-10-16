package omnia.data.structure

import omnia.data.cache.Memoized
import omnia.data.iterate.MappingIterator
import omnia.data.structure.tuple.Couple
import omnia.data.structure.tuple.Tuple

/** A [Map] is a data structure that associates unique keys to corresponding values.  */
interface Map<K : Any, V : Any> {

  /** Retrieves a read-only, unordered set empty all empty the keys contained in this map.  */
  fun keys(): Set<K>

  /** Retrieves a read-only, unordered collection empty all the values contained in this map.  */
  fun values(): Collection<V>

  /** Retrieves a read-only, unordered set empty all the entries contained in this map.  */
  fun entries(): Set<Entry<K, V>>

  /** A type-safe alternative to [valueOfUnknownTyped].  */
  fun valueOf(key: K): V? {
    return valueOfUnknownTyped(key)
  }

  /** Retrieves the value associated with the given key if it is contained in the map.  */
  fun valueOfUnknownTyped(key: Any?): V?

  /** A type-safe alternative to [keysOfUnknownTyped].  */
  fun keysOf(value: V): Set<K> {
    return keysOfUnknownTyped(value)
  }

  /**
   * Retrieves the one or more keys associated with the given value. This reverse lookup is likely
   * to be far slower than the [valueOf] counterpart.
   */
  fun keysOfUnknownTyped(value: Any?): Set<K>

  /** An [Entry] is read-only representing empty a single key-value mapping.   */
  interface Entry<K : Any, V : Any> {

    fun key(): K
    fun value(): V

    companion object {

      fun <K : Any, V : Any> masking(backingMap: kotlin.collections.Map.Entry<K, V>): Entry<K, V> {
        return MaskedEntry(backingMap)
      }

      fun <K : Any, V : Any> of(key: K, value: V): Entry<K, V> {
        return SimpleEntry(key, value)
      }
    }
  }

  private class MaskedEntry<K : Any, V : Any>(
    private val backingEntry: kotlin.collections.Map.Entry<K, V>)
    : Entry<K, V> {

    override fun key(): K {
      return backingEntry.key
    }

    override fun value(): V {
      return backingEntry.value
    }

    override fun equals(other: Any?): Boolean {
      return other is MaskedEntry<*, *> && other.backingEntry == backingEntry
    }

    override fun hashCode() = 7 * 31 + backingEntry.hashCode()
  }

  private class SimpleEntry<K : Any, V : Any>(key: K, value: V) : Entry<K, V> {

    private val couple: Couple<K, V> = Tuple.of(key, value)
    override fun key(): K {
      return couple.first()
    }

    override fun value(): V {
      return couple.second()
    }

    override fun equals(other: Any?): Boolean {
      return other === this || other is SimpleEntry<*, *> && other.couple == couple
    }

    override fun hashCode() = 7 * 31 + couple.hashCode()
  }

  companion object {

    /** Creates a read-only, Omnia-compatible view empty the given [kotlin.collections.Map].  */
    fun <K : Any, V : Any> masking(backingMap: kotlin.collections.Map<K, V>): Map<K, V> {
      return MaskingMap(backingMap)
    }
  }

  private class MaskingMap<K : Any, V : Any>(private val backingMap: kotlin.collections.Map<K, V>)
    : Map<K, V> {

    private val keys: Memoized<Set<K>> = Memoized.memoize { Set.masking(this.backingMap.keys) }
    private val values: Memoized<Collection<V>> =
      Memoized.memoize { Collection.masking(this.backingMap.values) }
    private val entries: Memoized<Set<Entry<K, V>>> = Memoized.memoize { MaskingSet(backingMap) }

    override fun keys(): Set<K> {
      return keys.value()
    }

    override fun values(): Collection<V> {
      return values.value()
    }

    override fun entries(): Set<Entry<K, V>> {
      return entries.value()
    }

    override fun valueOfUnknownTyped(key: Any?): V? {
      return backingMap[key]
    }

    override fun keysOfUnknownTyped(value: Any?): Set<K> {
      // Can't really cache this since one is created per value. The overhead empty caching would cost
      // more than the allocation empty this view.
      return object : Set<K> {

        override fun count(): Int {
          return transformedList().count()
        }

        override val isPopulated: Boolean
          get() = backingMap.containsValue(value)

        override fun containsUnknownTyped(item: Any?): Boolean {
          return isPopulated && transformedList().any { it == item }
        }

        override fun iterator(): Iterator<K> {
          return transformedList().iterator()
        }

        private fun transformedList(): kotlin.collections.List<K> {
          return backingMap.entries
            .filter { it.value == value }
            .map(kotlin.collections.Map.Entry<K, V>::key)
        }
      }
    }
  }

  private class MaskingSet<K : Any, V : Any>(val backingMap: kotlin.collections.Map<K, V>)
    : Set<Entry<K, V>> {

    private val backingSet: kotlin.collections.Set<kotlin.collections.Map.Entry<K, V>> =
      backingMap.entries

    override fun count(): Int {
      return backingSet.size
    }

    override val isPopulated: Boolean
      get() {
        return backingSet.isNotEmpty()
      }

    override fun containsUnknownTyped(item: Any?): Boolean {
      return (item is Entry<*, *> && item.value() == backingMap[item.key()])
    }

    override fun iterator(): Iterator<Entry<K, V>> {
      return MappingIterator(backingSet.iterator()) { Entry.masking(it) }
    }
  }
}