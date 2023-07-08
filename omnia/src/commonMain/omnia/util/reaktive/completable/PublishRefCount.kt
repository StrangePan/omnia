package omnia.util.reaktive.completable

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.asObservable
import com.badoo.reaktive.observable.asCompletable
import com.badoo.reaktive.observable.publish
import com.badoo.reaktive.observable.refCount

fun Completable.publishRefCount(subscriberCount: Int = 1) =
  this.asObservable<Unit>().publish().refCount(subscriberCount).asCompletable()