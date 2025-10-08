package omnia.io.filesystem.resources

import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.PathComponent
import omnia.io.filesystem.sandbox.SandboxDirectory
import omnia.io.filesystem.sandbox.SandboxFile

actual class ResourceDirectory(
  actual override val fileSystem: ResourceFileSystem,
  private val sandboxDirectory: SandboxDirectory):
  Directory, ResourceFileSystemObject {

  actual override val name get() = fullPath.components.last()

  actual override val fullPath get() = sandboxDirectory.fullPath

  actual override val parentDirectory get() =
    sandboxDirectory.parentDirectory?.let { ResourceDirectory(fileSystem, it) }

  actual override val parentDirectories: Iterable<ResourceDirectory> get() =
    sandboxDirectory.parentDirectories.map { ResourceDirectory(fileSystem, it) }

  actual override val contents: Iterable<ResourceFileSystemObject> get() =
    sandboxDirectory.contents.map {
      when (it) {
        is SandboxDirectory -> ResourceDirectory(fileSystem, it)
        is SandboxFile -> ResourceFile(fileSystem, it)
      }
    }

  actual override val files: Iterable<ResourceFile> get() =
    sandboxDirectory.files.map { ResourceFile(fileSystem, it) }

  actual override val subdirectories: Iterable<ResourceDirectory> get() =
    sandboxDirectory.subdirectories.map { ResourceDirectory(fileSystem, it) }

  // TODO create a read-only file system interface
  actual override fun createFile(name: PathComponent): ResourceFile =
    throw UnsupportedOperationException("Not permitted to create files in the application resources.")

  // TODO create a read-only file system interface
  actual override fun createSubdirectory(name: PathComponent): ResourceDirectory =
    throw UnsupportedOperationException("Not permitted to create directories in the application resources.")

  // TODO create a read-only file system interface
  actual override fun delete(): Unit =
    throw UnsupportedOperationException("Not permitted to delete directories in the application resources.")

  // TODO create a read-only file system interface
  actual override fun moveTo(path: AbsolutePath): Unit =
    throw UnsupportedOperationException("Not permitted to move directories in the application resources.")

  // TODO create a read-only file system interface
  actual override fun copyTo(path: AbsolutePath): ResourceDirectory =
    throw UnsupportedOperationException("Not permitted to copy directories in the application resources.")
}
