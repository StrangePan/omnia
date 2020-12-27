package omnia.data.structure.mutable

import java.util.Objects
import java.util.Optional
import kotlin.math.max

class ArrayQueue<E> private constructor(capacity: Int = INITIAL_CAPACITY) : Queue<E> {
    private val minimumCapacity: Int
    private var subQueue: FixedArrayQueue<E>
    override fun enqueue(item: E) {
        Objects.requireNonNull(item)
        if (subQueue.capacity() == subQueue.count()) {
            subQueue = FixedArrayQueue(subQueue.capacity() * 2, subQueue)
        }
        subQueue.enqueue(item)
    }

    override fun dequeue(): Optional<E> {
        val item = subQueue.dequeue()
        if (subQueue.capacity() > minimumCapacity && subQueue.count() <= subQueue.capacity() / 4) {
            subQueue = FixedArrayQueue(max(subQueue.capacity() / 2, minimumCapacity), subQueue)
        }
        return item
    }

    override fun peek(): Optional<E> {
        return subQueue.peek()
    }

    override fun count(): Int {
        return subQueue.count()
    }

    override val isPopulated: Boolean
        get() = subQueue.isPopulated

    private class FixedArrayQueue<E>(capacity: Int) : Queue<E> {
        private val items: Array<Any?>
        private var head = 0
        private var tail = 0

        constructor(capacity: Int, other: FixedArrayQueue<E>) : this(capacity) {
            require(other.count() <= capacity) {
                String.format(
                        "Attempted to copy a queue (capacity %d) into a smaller queue (capacity %d)",
                        other.capacity(),
                        capacity)
            }
            var item = other.dequeue()
            while (item.isPresent) {
                enqueue(item.get())
                item = other.dequeue()
            }
        }

        override fun enqueue(item: E) {
            Objects.requireNonNull(item)
            check(items[tail] == null) { String.format("Attempted to enqueue an item into a full queue (size %d)", items.size) }
            items[tail] = item
            tail = (tail + 1) % items.size
        }

        override fun dequeue(): Optional<E> {
            val item = items[head]
            items[head] = null
            if (item != null) {
                head = (head + 1) % items.size
            }
            @Suppress("UNCHECKED_CAST")
            return Optional.ofNullable(item as E)
        }

        override fun peek(): Optional<E> {
            @Suppress("UNCHECKED_CAST")
            return Optional.ofNullable(items[head] as E)
        }

        override fun count(): Int {
            if (head < tail) {
                return tail - head
            }
            if (head > tail) {
                return tail + items.size - head
            }
            return if (items[tail] != null) {
                items.size
            } else 0
        }

        override val isPopulated: Boolean
            get() {
                return items[head] != null
            }

        fun capacity(): Int {
            return items.size
        }

        init {
            require(capacity >= 1) { String.format("Capacity must be at least 1 (%d given)", capacity) }
            this.items = arrayOfNulls(capacity)
        }
    }

    companion object {
        private const val INITIAL_CAPACITY = 16
        @kotlin.jvm.JvmStatic
        fun <E> create(): ArrayQueue<E> {
            return ArrayQueue()
        }

        @kotlin.jvm.JvmStatic
        fun <E> createWithInitialCapacity(capacity: Int): ArrayQueue<E> {
            return ArrayQueue(capacity)
        }
    }

    init {
        require(capacity >= 1) { String.format("Capacity must be at least 1: %d given.", capacity) }
        minimumCapacity = capacity
        subQueue = FixedArrayQueue(capacity)
    }
}