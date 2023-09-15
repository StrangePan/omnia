package omnia.io.filesystem

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import omnia.io.Buffer

class FileBuffer(val file: File): Buffer {

  override fun overwriteLinesWith(data: Observable<String>): Completable = file.clearAndWriteLines(data)

  override fun readLines(): Observable<String> = file.readLines()
}
