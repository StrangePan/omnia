package omnia.io.filesystem.resources

import com.badoo.reaktive.completable.completableOfError
import com.badoo.reaktive.observable.Observable
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.File
import omnia.io.filesystem.sandbox.SandboxFile

actual class ResourceFile(
  actual override val fileSystem: ResourceFileSystem,
  private val sandboxFile: SandboxFile):
  File, ResourceFileSystemObject {

  actual override val name get() = fullPath.components.last()

  actual override val fullPath get() = sandboxFile.fullPath

  actual override val directory get() = fileSystem.getDirectoryAt(fullPath - 1)

  // TODO create a read-only file system interface
  actual override fun clearAndWriteLines(lines: Observable<String>) =
    completableOfError(UnsupportedOperationException("Not permitted to overwrite application resource contents."))

  actual override fun readLines() = sandboxFile.readLines()

  // TODO create a read-only file system interface
  actual override fun delete(): Unit =
    throw UnsupportedOperationException("Not permitted to delete files in the application resources.")

  // TODO create a read-only file system interface
  actual override fun moveTo(path: AbsolutePath): Unit =
    throw UnsupportedOperationException("Not permitted to move files in the application resources.")

  // TODO create a read-only file system interface
  actual override fun copyTo(path: AbsolutePath): ResourceFile =
    throw UnsupportedOperationException("Not permitted to copy files in the application resources.")
}
