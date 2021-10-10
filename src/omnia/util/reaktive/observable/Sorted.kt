package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.toList
import com.badoo.reaktive.single.flatMapIterable

fun <T> Observable<T>.sorted(comparator: Comparator<T>) =
  this.toList().flatMapIterable { it.sortedWith(comparator) }