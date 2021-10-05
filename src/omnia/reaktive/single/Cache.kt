package omnia.reaktive.single

import com.badoo.reaktive.observable.firstOrError
import com.badoo.reaktive.observable.replay
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.asObservable

fun <T> Single<T>.cache() = this.asObservable().replay(1).firstOrError()