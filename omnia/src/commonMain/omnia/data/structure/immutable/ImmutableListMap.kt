package omnia.data.structure.immutable

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.data.iterate.map
import omnia.data.structure.List
import omnia.data.structure.ListMap
import omnia.data.structure.ListSet
import omnia.data.structure.Map
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.data.structure.tuple.Couple
import omnia.data.structure.tuple.Tuple

/** A map+list composite data structure. */
class ImmutableListMap<K: Any, V: Any> private constructor(
  private val list: ImmutableList<K>,
  private val map: ImmutableMap<K, V>):
  ListMap<K, V> {

    init {
      check(list.count == map.keys.count)
    }

  override val keys: ListSet<K> get() =
    object : ListSet<K> {
      override fun containsUnknownTyped(item: Any?) = map.keys.containsUnknownTyped(item)

      override val count get() = list.count

      override fun iterator() = list.iterator()

      override fun itemAt(index: Int) = list.itemAt(index)

      override fun indexOf(item: Any?) = list.indexOf(item)
    }

  override val values: List<V> get() =
    object: List<V> {
      override fun containsUnknownTyped(item: Any?) = map.values.containsUnknownTyped(item)

      override val count get() = this@ImmutableListMap.count

      override fun iterator() = list.iterator().map { map.valueOf(it)!! }

      override fun itemAt(index: Int) = list.itemAt(index).let(map::valueOf)!!

      override fun indexOf(item: Any?) =
        list.indexOfFirst { key -> map.valueOf(key) == item }
    }

  override val entries: ListSet<Map.Entry<K, V>> get() = this

  override fun valueOfUnknownTyped(key: Any?): V? = map.valueOfUnknownTyped(key)

  override fun keysOf(value: V): ImmutableListSet<K> =
    keysOfUnknownTyped(value)

  override fun keysOfUnknownTyped(value: Any?): ImmutableListSet<K> =
    map.keysOfUnknownTyped(value).let { list.filter(it::contains) }.toImmutableListSet()

  override fun containsUnknownTyped(item: Any?) = indexOf(item) != null

  override val count: Int get() = list.count

  override fun iterator() = (0..<list.count).iterator().map(::Entry)

  override fun itemAt(index: Int): Map.Entry<K, V> = index.also(::checkIndexInRange).let(::Entry)

  override fun indexOf(item: Any?): Int? {
    if (item is ImmutableListMap<*, *>.Entry) {
      return if ((0..<list.count).contains(item.index) && item == Entry(item.index)) {
        item.index
      } else {
        null
      }
    }
    if (item is Map.Entry<*, *>) {
      val value = map.valueOfUnknownTyped(item.key)
      if (value != null) {
        val index = list.indexOf(value)
        if (index != null && Entry(index) == item) {
          return index
        }
      }
    }
    return (0..<list.count).indexOfFirst { Entry(it) == item }.takeIf { it >= 0 }
  }

  override fun equals(other: Any?) =
    other === this
      || other is ImmutableListMap<*, *>
        && other.list == list
        && other.map == map

  override fun hashCode() = hash(list, map)

  override fun toString() = "ImmutableMap($count)[${entries.joinToString { "{$it}" }}]"

  private fun checkIndexInRange(index: Int) {
    if (!(0..<list.count).contains(index)) {
      throw IndexOutOfBoundsException("Index $index is outside the range of 0..<$count")
    }
  }

  inner class Entry(val index: Int) : Map.Entry<K, V> {

    override val key get() = list.itemAt(index)
    override val value get() = map.valueOf(key)!!

    override fun equals(other: Any?) =
      other === this || other is Map.Entry<*, *> && other.key == key && other.value == value

    override fun hashCode() = hash(key, value)

    override fun toString() = "[$index] $key => $value"
  }

  fun toBuilder() = ImmutableListMap.builder<K, V>().addAll(this.map { Tuple.of(it.key, it.value) })

  class Builder<K: Any, V: Any> {
    val list = kotlin.collections.ArrayList<K>()
    val map = kotlin.collections.HashMap<K, V>()
    val keyIndex = kotlin.collections.HashMap<K, Int>()

    fun addAll(iterable: Iterable<Couple<K, V>>): Builder<K, V> {
      iterable.forEach { addMapping(it.first, it.second) }
      return this
    }

    fun addMapping(key: K, value: V): Builder<K, V> {
      remove(key)
      map.put(key, value)
      list.add(key)
      keyIndex[key] = list.size - 1
      check(list.size == map.size)
      check(list.size == keyIndex.size)
      return this
    }

    fun addMappingIfAbsent(key: K, value: V): Builder<K, V> {
      return if (!map.contains(key)) {
        addMapping(key, value)
      } else {
        this
      }
    }

    fun insertMappingAt(index: Int, key: K, value: V): Builder<K, V> {
      if (keyIndex[key] == index) {
        return replaceMappingIfPresent(key, value)
      }
      remove(key)
      map[key] = value
      list.add(index, key)
      keyIndex[key] = index
      (index + 1 ..< list.size).forEach { keyIndex[list[it]] = it }
      check(list.size == map.size)
      check(list.size == keyIndex.size)
      return this
    }

    fun addOrReplaceMapping(key: K, value: V): Builder<K, V> {
      return if (map.contains(key)) {
        replaceMappingIfPresent(key, value)
      } else {
        addMapping(key, value)
      }
    }

    fun replaceMappingIfPresent(key: K, value: V): Builder<K, V> {
      if (map.contains(key)) {
        check(list[keyIndex[key]!!] == key)
        map[key] = value
      }
      return this
    }

    fun remove(key: K): Builder<K, V> {
      if (map.remove(key) != null) {
        val oldIndex = keyIndex.remove(key)!!
        check(list[oldIndex] == key)
        (oldIndex + 1..<list.size).forEach { keyIndex[list[it]] = it }
        list.removeAt(oldIndex)
        check(list.size == map.size)
        check(list.size == keyIndex.size)
      }
      return this
    }

    fun build(): ImmutableListMap<K, V> =
      ImmutableListMap(list.toImmutableList(), map.toImmutableMap())
  }

  companion object {

    private val EMPTY_IMMUTABLE_LIST_MAP: ImmutableListMap<*, *> = ImmutableListMap<Any, Any>(ImmutableList.empty(), ImmutableMap.empty())

    @Suppress("UNCHECKED_CAST")
    fun <K: Any, V: Any> empty(): ImmutableListMap<K, V> = EMPTY_IMMUTABLE_LIST_MAP as ImmutableListMap<K, V>

    fun <K: Any, V: Any> of(key: K, value: V): ImmutableListMap<K, V> =
      ImmutableListMap(ImmutableList.of(key), ImmutableMap.of(key, value))

    fun <K: Any, V: Any> copyOf(otherMap: ListMap<out K, out V>): ImmutableListMap<K, V> =
      if (otherMap is ImmutableListMap<*, *>) {
        @Suppress("UNCHECKED_CAST")
        otherMap as ImmutableListMap<K, V>
      } else {
        copyOf(otherMap.entries)
      }

    fun <K: Any, V: Any> copyOf(iterable: Iterable<Map.Entry<K, V>>): ImmutableListMap<K, V> =
      builder<K, V>().addAll(iterable.map { Tuple.of(it.key, it.value) }).build()

    fun <K: Any, V: Any> builder() = Builder<K, V>()
  }
}

fun <K: Any, V: Any> Iterable<Couple<K, V>>.toImmutableListMap() =
  ImmutableListMap.builder<K, V>().addAll(this).build()
