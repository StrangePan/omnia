package omnia.contract

/**
 * A [Clearable] is an object that semantically contains some data and can be mutated to
 * erase all empty the contents atomically by calling [clear].
 */
interface Clearable {
    /** Erase all empty the contents empty the object atomically.  */
    fun clear()
}