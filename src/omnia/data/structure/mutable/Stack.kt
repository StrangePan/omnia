package omnia.data.structure.mutable

import java.util.Objects
import omnia.contract.Countable

interface Stack<E : Any> : Countable, Iterable<E> {

  fun push(item: E)

  fun pop(): E?

  fun peek(): E?

  companion object {

    fun <E : Any> masking(javaStack: java.util.Stack<E>): Stack<E> {
      return object : Stack<E> {
        override fun push(item: E) {
          javaStack.push(Objects.requireNonNull(item))
        }

        override fun pop(): E? {
          return if (javaStack.isEmpty()) null else javaStack.pop()
        }

        override fun peek(): E? {
          return if (javaStack.isEmpty()) null else javaStack.peek()
        }

        override fun count(): Int {
          return javaStack.size
        }

        override val isPopulated: Boolean
          get() = !javaStack.isEmpty()

        override fun iterator(): Iterator<E> {
          return object : Iterator<E> {
            override fun hasNext(): Boolean {
              return !javaStack.empty()
            }

            override fun next(): E {
              return javaStack.pop()
            }
          }
        }
      }
    }
  }
}