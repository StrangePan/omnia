package omnia.data.structure.mutable

import kotlin.collections.MutableList as KotlinMutableList
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

    fun <E : Any> masking(backingList: KotlinMutableList<E>): Queue<E> {
      return object : Queue<E> {
        override fun dequeue(): E? {
          return backingList.removeFirstOrNull()
        }

        override fun peek(): E? {
          return backingList.firstOrNull()
        }

        override fun enqueue(item: E) {
          backingList.add(item)
        }

        override fun count(): Int {
          return backingList.size
        }

        override val isPopulated: Boolean
          get() = backingList.isNotEmpty()

        override fun iterator(): Iterator<E> {
          return object : Iterator<E> {
            override fun hasNext(): Boolean {
              return backingList.isNotEmpty()
            }

            override fun next(): E {
              return backingList.removeFirst()
            }
          }
        }
      }
    }
  }
}