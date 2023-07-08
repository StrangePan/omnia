package omnia.io

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable

class FileBuffer(val file: File): Buffer {

  override fun overwriteLinesWith(data: Observable<String>): Completable = file.clearAndWriteLines(data)

  override fun readLines(): Observable<String> = file.readLines()
}