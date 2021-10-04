package omnia.data.structure.mutable

import omnia.contract.Countable

interface Stack<E : Any> : Countable, Iterable<E> {

  fun push(item: E)

  fun pop(): E?

  fun peek(): E?

  companion object {

    fun <E : Any> masking(kotlinList: kotlin.collections.MutableList<E>): Stack<E> {
      return object : Stack<E> {
        override fun push(item: E) {
          kotlinList.add(item)
        }

        override fun pop(): E? {
          return kotlinList.removeLastOrNull()
        }

        override fun peek(): E? {
          return kotlinList.lastOrNull()
        }

        override fun count(): Int {
          return kotlinList.size
        }

        override val isPopulated: Boolean get() = kotlinList.isNotEmpty()

        override fun iterator(): Iterator<E> {
          return object : Iterator<E> {
            override fun hasNext(): Boolean {
              return kotlinList.isNotEmpty()
            }

            override fun next(): E {
              return kotlinList.removeLast()
            }
          }
        }
      }
    }
  }
}