package omnia.reaktive.completable

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.asObservable
import com.badoo.reaktive.observable.asCompletable
import com.badoo.reaktive.observable.autoConnect
import com.badoo.reaktive.observable.replay

fun Completable.cache() = this.asObservable<Unit>().replay(1).autoConnect(1).asCompletable()