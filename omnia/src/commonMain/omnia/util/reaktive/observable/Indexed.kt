package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.notNull
import com.badoo.reaktive.observable.scan
import omnia.data.structure.tuple.Couple
import omnia.data.structure.tuple.Tuple

fun <T> Observable<T>.indexed() =
  this.scan(null) { lastCouple: Couple<Int, T>?, nextItem: T -> Tuple.of((lastCouple?.first ?: -1) + 1, nextItem) }
    .notNull()
