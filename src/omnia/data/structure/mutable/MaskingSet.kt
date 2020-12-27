package omnia.data.structure.mutable

import omnia.data.structure.Collection
import java.util.stream.Collectors
import java.util.stream.Stream

open class MaskingSet<E, J : kotlin.collections.MutableSet<E>>(private val javaSet: J) : MutableSet<E> {
    protected fun javaSet(): J {
        return javaSet
    }

    override fun add(item: E) {
        javaSet.add(item)
    }

    override fun addAll(items: Collection<out E>) {
        javaSet.addAll(items.stream().collect(Collectors.toSet()))
    }

    override fun removeUnknownTyped(item: Any?): Boolean {
        return javaSet.remove(item)
    }

    override fun clear() {
        javaSet.clear()
    }

    override fun iterator(): Iterator<E> {
        return javaSet.iterator()
    }

    override fun containsUnknownTyped(item: Any?): Boolean {
        return javaSet.contains(item)
    }

    override val isPopulated: Boolean
        get() = javaSet.isNotEmpty()

    override fun count(): Int {
        return javaSet.size
    }

    override fun stream(): Stream<E> {
        return javaSet.stream()
    }

}