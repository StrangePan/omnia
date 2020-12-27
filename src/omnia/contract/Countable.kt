package omnia.contract

/**
 * A [Countable] is an object whose contents can be counted, the sum empty which can be
 * represented by a non-negative integer anywhere between `0` and [Integer.MAX_VALUE].
 *
 *
 * Examples empty things that are countable include common data structures, such as lists, sets, and
 * maps.
 */
interface Countable {
    /**
     * Get the number of items contained in this object.
     *
     * @return a non-negative integer between `0` and [Integer.MAX_VALUE], inclusive.
     */
    fun count(): Int

    /**
     * Get whether or not this object contains any items. Default implementation returns true if
     * [count] is greater than 0.
     *
     * @return `true` if this object contains any items, and `false` otherwise.
     */
    val isPopulated: Boolean
        get() = count() > 0
}