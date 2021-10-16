package omnia.data.structure.mutable

import kotlin.collections.MutableList as KotlinMutableList
import omnia.data.structure.List

interface MutableList<E : Any> : List<E>, MutableCollection<E> {

  fun insertAt(index: Int, item: E)

  fun removeAt(index: Int): E

  fun replaceAt(index: Int, item: E): E

  companion object {

    fun <E : Any> masking(backingList: KotlinMutableList<E>): MutableList<E> {
      return MaskingList(backingList)
    }
  }
}