package omnia.util.reaktive.maybe

import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.asObservable
import com.badoo.reaktive.observable.autoConnect
import com.badoo.reaktive.observable.firstOrComplete
import com.badoo.reaktive.observable.replay

fun <T> Maybe<T>.cache() = this.asObservable().replay(1).autoConnect(1).firstOrComplete()
