package omnia.util.reaktive.completable

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.asObservable
import com.badoo.reaktive.observable.asCompletable
import com.badoo.reaktive.observable.autoConnect
import com.badoo.reaktive.observable.replay

fun Completable.cache(subscriberCount: Int = 1) =
  this.asObservable<Unit>().replay(1).autoConnect(subscriberCount).asCompletable()
