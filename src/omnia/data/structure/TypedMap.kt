package omnia.data.structure

import omnia.data.structure.immutable.ImmutableMap
import omnia.data.structure.mutable.HashMap
import omnia.data.structure.mutable.MutableMap
import java.util.Optional
import java.util.function.Consumer

class TypedMap private constructor(map: Map<Key<*>, Any?>) {
    class Key<V> {
        fun <V> create(): Key<V> {
            return Key()
        }
    }

    class Builder {
        private val map: MutableMap<Key<*>, Any?> = HashMap.create()
        fun <V> putMapping(key: Key<V>, value: V): Builder {
            map.putMapping(key, value)
            return this
        }

        fun putMappings(map: TypedMap): Builder {
            map.map.entries().forEach(Consumer { entry -> this.map.putMapping(entry.key(), entry.value()) })
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

    private val map: Map<Key<*>, Any?>
    operator fun <V> get(key: Key<V>): Optional<V> {
        return map.valueOf(key).map { v ->
            @Suppress("UNCHECKED_CAST")
            v as V
        }
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

        fun <V> of(key: Key<V>, value: V): TypedMap {
            return TypedMap(ImmutableMap.of(key, value))
        }
    }

    init {
        this.map = ImmutableMap.copyOf(map)
    }
}