package omnia.util.reaktive.observable

import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.asSingleOrError
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.reduce
import com.badoo.reaktive.single.Single

fun <T> Observable<T>.last(): Maybe<T> {
  return this.reduce { _, a -> a }
}

fun <T> Observable<T>.lastOrError(): Single<T> {
  return this.reduce { _, a -> a }.asSingleOrError()
}
