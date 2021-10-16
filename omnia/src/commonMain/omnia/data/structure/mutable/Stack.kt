package omnia.data.structure.mutable

import omnia.contract.Countable

interface Stack<E : Any> : Countable, Iterable<E> {

  fun push(item: E)

  fun pop(): E?

  fun peek(): E?

  companion object {

    fun <E : Any> masking(backingList: kotlin.collections.MutableList<E>): Stack<E> {
      return object : Stack<E> {
        override fun push(item: E) {
          backingList.add(item)
        }

        override fun pop(): E? {
          return backingList.removeLastOrNull()
        }

        override fun peek(): E? {
          return backingList.lastOrNull()
        }

        override fun count(): Int {
          return backingList.size
        }

        override val isPopulated: Boolean get() = backingList.isNotEmpty()

        override fun iterator(): Iterator<E> {
          return object : Iterator<E> {
            override fun hasNext(): Boolean {
              return backingList.isNotEmpty()
            }

            override fun next(): E {
              return backingList.removeLast()
            }
          }
        }
      }
    }
  }
}