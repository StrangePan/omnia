package omnia.data.structure.mutable

import java.util.Objects
import java.util.Optional

class LinkedQueue<E: Any> : Queue<E> {
    private val items: MutableList<E> = LinkedList()
    override fun enqueue(item: E) {
        items.add(Objects.requireNonNull(item))
    }

    override fun dequeue(): Optional<E> {
        if (!items.isPopulated) {
            return Optional.empty()
        }
        val item = items.itemAt(0)
        items.removeAt(0)
        return Optional.of(item)
    }

    override fun peek(): Optional<E> {
        return if (items.isPopulated) Optional.of(items.itemAt(0)) else Optional.empty()
    }

    override fun count(): Int {
        return items.count()
    }

    override val isPopulated: Boolean
        get() = items.isPopulated
}