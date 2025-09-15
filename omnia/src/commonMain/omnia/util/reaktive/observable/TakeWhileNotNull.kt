package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.notNull
import com.badoo.reaktive.observable.takeWhile

fun <T: Any> Observable<T?>.takeWhileNotNull(): Observable<T> =
  this.takeWhile { it != null }.notNull()
