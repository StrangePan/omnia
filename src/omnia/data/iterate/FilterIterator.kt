package omnia.data.iterate

import java.util.NoSuchElementException
import java.util.Objects
import java.util.function.Predicate

class FilterIterator<E>(source: MutableIterator<E?>?, filter: Predicate<in E?>?) : MutableIterator<E?> {
    private val source: MutableIterator<E?>?
    private val filter: Predicate<in E?>?
    private var didPeekNext = false
    private var peekedNext: E? = null
    private var hasNext = false
    override fun hasNext(): Boolean {
        maybePeekNext()
        return hasNext
    }

    override fun next(): E? {
        maybePeekNext()
        if (!hasNext) {
            throw NoSuchElementException("invoked next() when there is no next")
        }
        return clearNext()
    }

    private fun maybePeekNext() {
        if (didPeekNext) {
            return
        }
        didPeekNext = true
        hasNext = false
        while (source!!.hasNext()) {
            peekedNext = source.next()
            if (filter!!.test(peekedNext)) {
                hasNext = true
                break
            }
        }
    }

    private fun clearNext(): E? {
        didPeekNext = false
        val next = peekedNext
        peekedNext = null
        return next
    }

    override fun remove() {
        check(!didPeekNext) {
            ("Cannot invoke remove() after calling hasNext(). This is a limitation of "
                    + "FilterIterator.")
        }
        source!!.remove()
    }

    init {
        this.source = Objects.requireNonNull(source)
        this.filter = Objects.requireNonNull(filter)
    }
}