package omnia.contract

/**
 * Something that is [Invalidable] is one that exhibits observable behaviors that can be
 * rendered invalidated or outdated by calling [invalidate].
 *
 *
 * When an object is said to be valid, its private internal state or publicly observable state is
 * in a consistent and accurate state, fit for dependents to safely use said state for all purposes.
 * By contrast, an object that is said to be invalid is not fit for use by its dependents. Most
 * objects that are [Invalidable] should provide an explicit method for making oneself valid
 * again, although it is not an enforceable requirement.
 */
interface Invalidable {
    /**
     * Invalidates this object. This is a manual operation that should be called when a change in
     * the world causes this object to no longer be safely in a valid state.
     */
    fun invalidate()
}