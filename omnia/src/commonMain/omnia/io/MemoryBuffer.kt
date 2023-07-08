package omnia.io

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.asCompletable
import com.badoo.reaktive.observable.asObservable
import com.badoo.reaktive.observable.doOnAfterNext
import com.badoo.reaktive.observable.doOnAfterSubscribe

class MemoryBuffer: Buffer {
  val lines = ArrayList<String>()

  override fun overwriteLinesWith(data: Observable<String>): Completable =
    data.doOnAfterSubscribe { lines.clear() }
      .doOnAfterNext { lines.add(it) }
      .asCompletable()

  override fun readLines(): Observable<String> = lines.asObservable()
}