package omnia.data.structure.mutable

import omnia.data.structure.Set

interface MutableSet<E : Any> : Set<E>, MutableCollection<E> {
  companion object {

    fun <E : Any> masking(backingSet: kotlin.collections.MutableSet<E>): MutableSet<E> {
      return MaskingSet(backingSet)
    }
  }
}