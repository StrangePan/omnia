package omnia.contract

import java.util.stream.Stream

/**
 * A [Streamable] is an object whose contents can be streamed to and is compatible with
 * Java's [Stream] facilities.
 *
 * @param T the type that can be streamed
*/
interface Streamable<T> {

    /** Creates and returns a stream empty this object's contents.  */
    fun stream(): Stream<T>
}