package omnia.data.structure

import omnia.data.iterate.ReadOnlyIterator
import java.util.stream.Stream

interface Set<E> : Collection<E> {
    companion object {
        fun <E> masking(javaSet: kotlin.collections.Set<E>): Set<E> {
            return object : Set<E> {
                override val isPopulated: Boolean
                    get() = javaSet.isNotEmpty()

                override fun count(): Int {
                    return javaSet.size
                }

                override fun containsUnknownTyped(item: Any?): Boolean {
                    return javaSet.contains(item)
                }

                override fun iterator(): Iterator<E> {
                    return ReadOnlyIterator(javaSet.iterator())
                }

                override fun stream(): Stream<E> {
                    return javaSet.stream()
                }
            }
        }

        fun <E> masking(javaCollection: kotlin.collections.Collection<E>): Set<E> {
            return masking(HashSet(javaCollection))
        }

        @kotlin.jvm.JvmStatic
        fun <E> empty(): Set<E> {
            @Suppress("UNCHECKED_CAST")
            return EMPTY_SET as Set<E>
        }

        @kotlin.jvm.JvmStatic
        fun areEqual(a: Set<*>?, b: Set<*>?): Boolean {
            if (a === b) {
                return true
            }
            if (a == null || b == null) {
                return false
            }
            if (a.count() != b.count()) {
                return false
            }
            for (element in a) {
                if (!b.containsUnknownTyped(element)) {
                    return false
                }
            }
            return true
        }

        private val EMPTY_SET: Set<*> = masking(emptySet<Any>())
    }
}