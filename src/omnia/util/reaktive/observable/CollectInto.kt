package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.toList
import com.badoo.reaktive.single.map

fun <T, C> Observable<T>.collectInto(collectionSupplier: () -> C, accumulator: (C, T) -> C) =
  this.toList()
    .map { options ->
      val collection: C = collectionSupplier()
      options.forEach { accumulator(collection, it) }
      collection
    }

fun <T, C, R> Observable<T>.collectInto(
    collectionSupplier: () -> C, accumulator: (C, T) -> C, finalizer: (C) -> R) =
  this.collectInto(collectionSupplier, accumulator).map(finalizer)