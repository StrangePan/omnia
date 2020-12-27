package omnia.data.stream

import java.util.stream.Stream

object Streams {
    @SafeVarargs
    fun <T> concat(vararg streams: Stream<out T>): Stream<T> {
        if (streams.isEmpty()) {
            return Stream.empty()
        }
        if (streams.size == 1) {
            return streams[0].map { i: T -> i }
        }
        var concatenatedStream = streams[0].map { i: T -> i }
        for (i in 1 until streams.size) {
            concatenatedStream = Stream.concat(concatenatedStream, streams[i])
        }
        return concatenatedStream
    }
}