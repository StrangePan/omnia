package omnia.io

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.asCompletable
import com.badoo.reaktive.observable.asObservable
import com.badoo.reaktive.observable.doOnBeforeNext
import com.badoo.reaktive.observable.doOnBeforeSubscribe
import com.badoo.reaktive.observable.publish
import com.badoo.reaktive.observable.refCount

class MemoryBuffer: Buffer {
  val lines = ArrayList<String>()

  override fun overwriteLinesWith(data: Observable<String>): Completable =
    data.doOnBeforeSubscribe { lines.clear() }
      .doOnBeforeNext { lines.add(it) }
      .publish()
      .refCount()
      .asCompletable()

  override fun readLines(): Observable<String> = lines.asObservable()
}
