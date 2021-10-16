package omnia.data.structure.mutable

import omnia.data.structure.List

class ArrayList<E : Any> private constructor(
    backingList: kotlin.collections.List<E> = kotlin.collections.ArrayList()) :
    MaskingList<E>(toArrayList(backingList)) {

  companion object {

    fun <E : Any> of(vararg items: E): ArrayList<E> {
      return ArrayList(mutableListOf(*items))
    }

    fun <E : Any> copyOf(otherList: List<out E>): ArrayList<E> {
      val newList: ArrayList<E> = create()
      otherList.iterator().forEachRemaining { element: E -> newList.add(element) }
      return newList
    }

    fun <E : Any> create(): ArrayList<E> {
      return ArrayList()
    }

    fun <E : Any> toArrayList(list: kotlin.collections.List<E>): kotlin.collections.ArrayList<E> {
      return if (list is kotlin.collections.ArrayList) list else kotlin.collections.ArrayList(list)
    }
  }
}