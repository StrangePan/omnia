package omnia.data.structure

import java.util.stream.Stream
import omnia.data.iterate.ReadOnlyIterator

interface Set<E> : Collection<E> {
  companion object {

    fun <E> masking(kotlinSet: kotlin.collections.Set<E>): Set<E> {
      return object : Set<E> {
        override val isPopulated: Boolean
          get() = kotlinSet.isNotEmpty()

        override fun count(): Int {
          return kotlinSet.size
        }

        override fun containsUnknownTyped(item: Any?): Boolean {
          return kotlinSet.contains(item)
        }

        override fun iterator(): Iterator<E> {
          return ReadOnlyIterator(kotlinSet.iterator())
        }

        override fun stream(): Stream<E> {
          return kotlinSet.stream()
        }

        override fun toString(): String {
          return kotlinSet.toString()
        }
      }
    }

    fun <E> masking(kotlinCollection: kotlin.collections.Collection<E>): Set<E> {
      return masking(HashSet(kotlinCollection))
    }

    @JvmStatic
    fun <E> empty(): Set<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_SET as Set<E>
    }

    @JvmStatic
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