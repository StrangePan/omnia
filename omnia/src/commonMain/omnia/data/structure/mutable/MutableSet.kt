package omnia.data.structure.mutable

import kotlin.collections.MutableSet as KotlinMutableSet
import omnia.data.structure.Set

interface MutableSet<E : Any> : Set<E>, MutableCollection<E> {
  companion object {

    fun <E : Any> masking(backingSet: KotlinMutableSet<E>): MutableSet<E> {
      return MaskingSet(backingSet)
    }
  }
}