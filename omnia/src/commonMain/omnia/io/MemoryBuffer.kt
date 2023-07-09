package omnia.io

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.asCompletable
import com.badoo.reaktive.observable.asObservable
import com.badoo.reaktive.observable.doOnBeforeNext
import com.badoo.reaktive.observable.doOnBeforeSubscribe
import com.badoo.reaktive.observable.publish
import com.badoo.reaktive.observable.refCount
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import omnia.data.structure.List
import omnia.data.structure.mutable.ArrayList
import omnia.util.reaktive.observable.collectIntoArrayList

class MemoryBuffer private constructor(val lines: ArrayList<String>): Buffer {

  constructor() : this(ArrayList.create())

  constructor(lines: List<String>) : this(ArrayList.copyOf(lines))

  override fun overwriteLinesWith(data: Observable<String>): Completable =
    data.doOnBeforeSubscribe { lines.clear() }
      .doOnBeforeNext { lines.add(it) }
      .publish()
      .refCount()
      .asCompletable()

  override fun readLines(): Observable<String> = lines.asObservable()

  companion object {
    fun copyOf(buffer: Buffer): Single<MemoryBuffer> {
      return buffer.readLines().collectIntoArrayList().map(::MemoryBuffer)
    }
  }

  override fun toString(): String {
    return lines.joinToString(separator = "\n", postfix = "\n")
  }
}
