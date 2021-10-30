package omnia.io

import com.badoo.reaktive.observable.Observable

/** An interface for interfacing with file system files.  */
expect class File {

  fun clearAndWriteLines(lines: Observable<String>): Observable<String>

  fun readLines(): Observable<String>

  companion object {
    fun fromPath(path: String): File
  }
}