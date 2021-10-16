package omnia.data.structure

import omnia.data.iterate.ReadOnlyIterator

interface Set<E : Any> : Collection<E> {
  companion object {

    fun <E : Any> masking(backingSet: kotlin.collections.Set<E>): Set<E> {
      return object : Set<E> {
        override val isPopulated: Boolean
          get() = backingSet.isNotEmpty()

        override fun count(): Int {
          return backingSet.size
        }

        override fun containsUnknownTyped(item: Any?): Boolean {
          return backingSet.contains(item)
        }

        override fun iterator(): Iterator<E> {
          return ReadOnlyIterator(backingSet.iterator())
        }

        override fun toString(): String {
          return backingSet.toString()
        }
      }
    }

    fun <E : Any> masking(backingCollection: kotlin.collections.Collection<E>): Set<E> {
      return masking(HashSet(backingCollection))
    }

    @JvmStatic
    fun <E : Any> empty(): Set<E> {
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

    private val EMPTY_SET: Set<*> = masking(emptySet())
  }
}