package omnia.data.structure.mutable

import java.util.Objects
import java.util.Optional
import omnia.contract.Countable

interface Queue<E> : Countable, Iterable<E> {

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

    fun <E> masking(javaQueue: java.util.Queue<E>): Queue<E> {
      return object : Queue<E> {
        override fun dequeue(): Optional<E> {
          return Optional.ofNullable(javaQueue.poll())
        }

        override fun peek(): Optional<E> {
          return Optional.ofNullable(javaQueue.peek())
        }

        override fun enqueue(item: E) {
          javaQueue.add(Objects.requireNonNull(item))
        }

        override fun count(): Int {
          return javaQueue.size
        }

        override val isPopulated: Boolean
          get() = !javaQueue.isEmpty()

        override fun iterator(): Iterator<E> {
          return object : Iterator<E> {
            override fun hasNext(): Boolean {
              return !javaQueue.isEmpty()
            }

            override fun next(): E {
              return javaQueue.remove()
            }
          }
        }
      }
    }
  }
}