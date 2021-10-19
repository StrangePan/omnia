package omnia.data.structure.mutable

import kotlin.collections.ArrayList as KotlinArrayList
import kotlin.collections.List as KotlinList
import omnia.data.structure.List

class ArrayList<E : Any> private constructor(
    backingList: KotlinList<E> = KotlinArrayList()
) :
    MaskingList<E>(toArrayList(backingList)) {

  companion object {

    fun <E : Any> of(vararg items: E): ArrayList<E> {
      return ArrayList(mutableListOf(*items))
    }

    fun <E : Any> copyOf(otherList: List<out E>): ArrayList<E> {
      val newList: ArrayList<E> = create()
      otherList.iterator().forEach(newList::add)
      return newList
    }

    fun <E : Any> create(): ArrayList<E> {
      return ArrayList()
    }

    fun <E : Any> toArrayList(list: KotlinList<E>): KotlinArrayList<E> {
      return if (list is KotlinArrayList) list else KotlinArrayList(list)
    }
  }
}