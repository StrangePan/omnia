package omnia.data.iterate

import java.util.Objects
import java.util.function.Consumer

/**
 * An [Iterator] that delegates to another source [Iterator], but does not support
 * any operations that would mutate the underlying data structure.
 */
class ReadOnlyIterator<E>(maskedIterator: Iterator<E>) : Iterator<E> {
    private val maskedIterator: Iterator<E> = Objects.requireNonNull(maskedIterator)

    override fun hasNext(): Boolean {
        return maskedIterator.hasNext()
    }

    override fun next(): E {
        return maskedIterator.next()
    }

    override fun forEachRemaining(action: Consumer<in E>) {
        maskedIterator.forEachRemaining(action)
    }

}