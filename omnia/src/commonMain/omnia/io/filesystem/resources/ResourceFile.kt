package omnia.io.filesystem.resources

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.File
import omnia.io.filesystem.PathComponent

/** A representation of a file in the application package's resources. */
expect class ResourceFile: File, ResourceFileSystemObject {

  override val fileSystem: ResourceFileSystem

  override val name: PathComponent

  override val fullPath: AbsolutePath

  override val directory: ResourceDirectory

  override fun clearAndWriteLines(lines: Observable<String>): Completable

  override fun readLines(): Observable<String>

  override fun delete()

  override fun moveTo(path: AbsolutePath)

  override fun copyTo(path: AbsolutePath): ResourceFile
}
