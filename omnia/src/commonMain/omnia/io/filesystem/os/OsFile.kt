package omnia.io.filesystem.os

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.File
import omnia.io.filesystem.PathComponent

/** A representation of a file in the operating system's filesystem. */
expect class OsFile: File {

  override val name: PathComponent

  override val fullPath: AbsolutePath

  override val directory: OsDirectory

  override fun clearAndWriteLines(lines: Observable<String>): Completable

  override fun readLines(): Observable<String>

  override fun delete()

  override fun moveTo(path: AbsolutePath)

  override fun copyTo(path: AbsolutePath): OsFile
}
