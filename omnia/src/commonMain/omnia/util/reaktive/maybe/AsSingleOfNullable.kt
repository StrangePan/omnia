package omnia.util.reaktive.maybe

import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.asSingle
import com.badoo.reaktive.maybe.map

// Converts a Maybe of a non-nullable value to a Single where the empty Maybe maps to null.
fun <T: Any> Maybe<T>.asSingleOfNullable() = this.map { it as T? }.asSingle(null)
