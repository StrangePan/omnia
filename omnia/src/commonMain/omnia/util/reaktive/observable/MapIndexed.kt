package omnia.util.reaktive.observable

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.doOnAfterNext
import com.badoo.reaktive.observable.doOnBeforeNext
import com.badoo.reaktive.observable.doOnBeforeSubscribe
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.scan
import com.badoo.reaktive.observable.skip
import omnia.data.structure.tuple.Couple
import omnia.data.structure.tuple.Tuple

fun <T: Any, R> Observable<T>.map(mapper: (Int, T) -> R): Observable<R> =
  this.scan<T, Couple<Int, T?>>(Tuple.of(0, null)) { couple, value -> Tuple.of(couple.first + 1, value) }
      .skip(1) // scan emits the seed for some reason >:(
      .map { couple -> mapper(couple.first - 1, couple.second!!) }