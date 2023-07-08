package omnia.io

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable

interface Buffer {

  fun overwriteLinesWith(data: Observable<String>): Completable

  fun readLines(): Observable<String>
}