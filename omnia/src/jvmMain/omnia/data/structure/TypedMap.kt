package omnia.data.structure

import omnia.data.structure.immutable.ImmutableMap
import omnia.data.structure.mutable.HashMap
import omnia.data.structure.mutable.MutableMap

class TypedMap private constructor(map: Map<Key<*>, Any>) {
  class Key<V : Any> {

    fun <V : Any> create(): Key<V> {
      return Key()
    }
  }

  class Builder {

    private val map: MutableMap<Key<*>, Any> = HashMap.create()
    fun <V : Any> putMapping(key: Key<V>, value: V): Builder {
      map.putMapping(key, value)
      return this
    }

    fun putMappings(map: TypedMap): Builder {
      map.map.entries()
        .forEach { entry -> this.map.putMapping(entry.key(), entry.value()) }
      return this
    }

    fun remove(key: Key<*>): Builder {
      map.removeKey(key)
      return this
    }

    fun build(): TypedMap {
      return TypedMap(map)
    }
  }

  private val map: Map<Key<*>, Any>
  operator fun <V : Any> get(key: Key<V>): V? {
    // if user bypasses the type-safety of the generic system, we can't do anything to stop them
    @Suppress("UNCHECKED_CAST")
    return map.valueOf(key) as V?
  }

  fun toBuilder(): Builder {
    return Builder().putMappings(this)
  }

  companion object {

    fun builder(): Builder {
      return Builder()
    }

    fun of(): TypedMap {
      return TypedMap(ImmutableMap.empty())
    }

    fun <V : Any> of(key: Key<V>, value: V): TypedMap {
      return TypedMap(ImmutableMap.of(key, value))
    }
  }

  init {
    this.map = ImmutableMap.copyOf(map)
  }
}