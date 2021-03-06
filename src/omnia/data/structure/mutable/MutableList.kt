package omnia.data.structure.mutable

import omnia.data.structure.List

interface MutableList<E> : List<E>, MutableCollection<E> {

  fun insertAt(index: Int, item: E)
  fun removeAt(index: Int): E
  fun replaceAt(index: Int, item: E): E

  companion object {

    fun <E> masking(javaList: kotlin.collections.MutableList<E>): MutableList<E> {
      return MaskingList(javaList)
    }
  }
}