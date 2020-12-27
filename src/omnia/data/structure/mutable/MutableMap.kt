package omnia.data.structure.mutable

import omnia.data.structure.Map
import omnia.data.structure.Set
import java.util.Optional
import java.util.function.Supplier

interface MutableMap<K, V> : Map<K, V> {
    fun putMapping(key: K, value: V)
    fun putMappingIfAbsent(key: K, value: Supplier<V>): V
    fun removeKey(key: K): Optional<V> {
        return removeUnknownTypedKey(key)
    }

    fun removeUnknownTypedKey(key: Any?): Optional<V>

    override fun entries(): Set<Map.Entry<K, V>>

    companion object {
        fun <K, V> masking(javaMap: kotlin.collections.MutableMap<K, V>): MutableMap<K, V> {
            return MaskingMap(javaMap)
        }
    }
}