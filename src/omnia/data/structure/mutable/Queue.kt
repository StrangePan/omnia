package omnia.data.structure.mutable

import omnia.contract.Countable

interface Queue<E : Any> : Countable, Iterable<E> {

  fun enqueue(item: E)

  fun dequeue(): E?

  fun peek(): E?

  override fun iterator(): Iterator<E> {
    return object : Iterator<E> {
      override fun hasNext(): Boolean {
        return peek() != null
      }

      override fun next(): E {
        return dequeue()!!
      }
    }
  }

  companion object {

    fun <E : Any> masking(kotlinList: kotlin.collections.MutableList<E>): Queue<E> {
      return object : Queue<E> {
        override fun dequeue(): E? {
          return kotlinList.removeFirstOrNull()
        }

        override fun peek(): E? {
          return kotlinList.firstOrNull()
        }

        override fun enqueue(item: E) {
          kotlinList.add(item)
        }

        override fun count(): Int {
          return kotlinList.size
        }

        override val isPopulated: Boolean
          get() = kotlinList.isNotEmpty()

        override fun iterator(): Iterator<E> {
          return object : Iterator<E> {
            override fun hasNext(): Boolean {
              return kotlinList.isNotEmpty()
            }

            override fun next(): E {
              return kotlinList.removeFirst()
            }
          }
        }
      }
    }
  }
}