package omnia.io

import com.badoo.reaktive.observable.Observable

/** An interface for interfacing with file system files.  */
expect class File {

  /** The short name for the file. Does not include names of any directories. Cannot be empty. */
  val name: String

  /** The [Directory] containing this file. */
  val directory: Directory

  fun clearAndWriteLines(lines: Observable<String>): Observable<String>

  fun readLines(): Observable<String>

  companion object {
    fun fromPath(path: String): File
  }
}