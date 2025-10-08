package omnia.data.structure

/** A map whose entries have a defined order like a list. */
interface ListMap<K: Any, V: Any>: ListSet<Map.Entry<K, V>>, Map<K, V> {

  override val keys: ListSet<K>

  override val values: List<V>

  override val entries: ListSet<Map.Entry<K, V>>

  override fun keysOf(value: V): ListSet<K>

  override fun keysOfUnknownTyped(value: Any?): ListSet<K>
}
