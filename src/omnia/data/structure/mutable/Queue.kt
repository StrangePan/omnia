package omnia.data.structure.mutable

import java.util.Optional
import omnia.contract.Countable

interface Queue<E: Any> : Countable, Iterable<E> {

  fun enqueue(item: E)
  fun dequeue(): Optional<E>
  fun peek(): Optional<E>
  override fun iterator(): Iterator<E> {
    return object : Iterator<E> {
      override fun hasNext(): Boolean {
        return peek().isPresent
      }

      override fun next(): E {
        return dequeue().get()
      }
    }
  }

  companion object {

    fun <E: Any> masking(kotlinList: kotlin.collections.MutableList<E>): Queue<E> {
      return object : Queue<E> {
        override fun dequeue(): Optional<E> {
          return Optional.ofNullable(kotlinList.removeFirstOrNull())
        }

        override fun peek(): Optional<E> {
          return Optional.ofNullable(kotlinList.firstOrNull())
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