package omnia.io.filesystem.resources

import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.PathComponent
import omnia.io.filesystem.os.OsDirectory
import omnia.io.filesystem.os.OsFile

actual class ResourceDirectory(
  actual override val fileSystem: ResourceFileSystem,
  actual override val fullPath: AbsolutePath,
  private val osDirectory: OsDirectory):
  Directory, ResourceFileSystemObject {

  actual override val name get() = fullPath.components.last()

  actual override val parentDirectory: ResourceDirectory? get() =
    fullPath.takeUnless(AbsolutePath::isRoot)?.let { fileSystem.getDirectoryAt(it - 1) }

  actual override val parentDirectories: Iterable<ResourceDirectory> get() {
    val builder = ImmutableList.builder<ResourceDirectory>()
    var parent = parentDirectory
    while (parent != null) {
      builder.add(parent)
      parent = parent.parentDirectory
    }
    return builder.build()
  }

  actual override val contents: Iterable<ResourceFileSystemObject> get() =
    osDirectory.contents.map {
      return@map when (it) {
        is OsDirectory -> ResourceDirectory(fileSystem, fullPath + it.name, it)
        is OsFile -> ResourceFile(fileSystem, fullPath + it.name, it)
        else -> throw IllegalStateException()
      }
    }

  actual override val files: Iterable<ResourceFile> get() =
    osDirectory.files.map { ResourceFile(fileSystem, fullPath + it.name, it) }

  actual override val subdirectories: Iterable<ResourceDirectory> get() =
    osDirectory.subdirectories.map { ResourceDirectory(fileSystem, fullPath + it.name, it) }

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
