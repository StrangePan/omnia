package omnia.io

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable

/** An interface for interfacing with file system files.  */
expect class File: FileSystemObject {

  override val name: String

  override val fullName: String

  /** The [Directory] containing this file. */
  val directory: Directory

  fun clearAndWriteLines(lines: Observable<String>): Completable

  fun readLines(): Observable<String>

  companion object {
    fun fromPath(path: String): File
  }
}