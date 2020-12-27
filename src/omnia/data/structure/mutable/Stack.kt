package omnia.data.structure.mutable

import omnia.contract.Countable
import java.util.Objects
import java.util.Optional

interface Stack<E> : Countable, Iterable<E> {
    fun push(item: E)
    fun pop(): Optional<E>
    fun peek(): Optional<E>

    companion object {
        fun <E> masking(javaStack: java.util.Stack<E>): Stack<E> {
            return object : Stack<E> {
                override fun push(item: E) {
                    javaStack.push(Objects.requireNonNull(item))
                }

                override fun pop(): Optional<E> {
                    return if (javaStack.isEmpty()) Optional.empty() else Optional.ofNullable(javaStack.pop())
                }

                override fun peek(): Optional<E> {
                    return if (javaStack.isEmpty()) Optional.empty() else Optional.ofNullable(javaStack.peek())
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