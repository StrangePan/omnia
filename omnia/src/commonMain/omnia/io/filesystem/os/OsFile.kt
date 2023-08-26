package omnia.io.filesystem.os

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import omnia.io.filesystem.File

/** A representation of a file in the operating system's filesystem. */
expect class OsFile: File {

  override val name: String

  override val fullName: String

  override val directory: OsDirectory

  override fun clearAndWriteLines(lines: Observable<String>): Completable

  override fun readLines(): Observable<String>
}
