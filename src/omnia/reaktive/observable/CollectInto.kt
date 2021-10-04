package omnia.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.collect
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.flatMap
import com.badoo.reaktive.single.toSingle

fun <T, C> Observable<T>.collectInto(collectionSupplier: () -> C, accumulator: (C, T) -> C)
: Single<C> {
  return this.toSingle()
    .flatMap { observable -> observable.collect(collectionSupplier(), accumulator) }
}