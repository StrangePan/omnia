package omnia.data.structure.mutable

import omnia.data.structure.List

class ArrayList<E> private constructor(javaList: java.util.ArrayList<E> = java.util.ArrayList()) :
  MaskingList<E>(javaList) {

  companion object {

    @SafeVarargs
    fun <E> of(vararg items: E): ArrayList<E> {
      return ArrayList(java.util.ArrayList(listOf(*items)))
    }

    @JvmStatic
    fun <E> copyOf(otherList: List<out E>): ArrayList<E> {
      val newList: ArrayList<E> = create()
      otherList.iterator().forEachRemaining { element: E -> newList.add(element) }
      return newList
    }

    @JvmStatic
    fun <E> create(): ArrayList<E> {
      return ArrayList()
    }
  }
}