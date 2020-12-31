package omnia.data.structure.mutable

import omnia.data.structure.Set

interface MutableSet<E> : Set<E>, MutableCollection<E> {
  companion object {

    fun <E> masking(javaSet: kotlin.collections.MutableSet<E>): MutableSet<E> {
      return MaskingSet(javaSet)
    }
  }
}