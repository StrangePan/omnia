package omnia.io.filesystem.virtual

import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.PathComponent

/**
 * An in-memory virtual directory object. Useful for tests, intermediate migration steps, and other situations where
 * we want to run file-manipulation algorithms without accessing the operating system's storage.
 */
class VirtualDirectory internal constructor(private val fileSystem: VirtualFileSystem, override val fullPath: AbsolutePath):
  VirtualFileSystemObject, Directory {

  override val name: PathComponent get() =
    // TODO how should we handle the directory name of the root directory, which has no name
    fullPath.components.last()

  override val parentDirectory: VirtualDirectory? =
    if (fullPath.isRoot) {
      null
    } else {
      fileSystem.getDirectory(fullPath - 1)
    }

  override val parentDirectories: Iterable<VirtualDirectory> get() {
    val parentDirectories = ImmutableList.builder<VirtualDirectory>()
    var parent = parentDirectory
    while (parent != null)
    {
      parentDirectories.add(parent)
      parent = parent.parentDirectory
    }
    return parentDirectories.build()
  }

  override val files: Iterable<VirtualFile> get() =
    fileSystem.getFilesInDirectory(this)

  override val subdirectories: Iterable<VirtualDirectory> get() =
    fileSystem.getDirectoriesInDirectory(this)

  override fun createFile(name: PathComponent): VirtualFile =
    fileSystem.createFile(fullPath + name)

  override fun createSubdirectory(name: PathComponent): VirtualDirectory =
    fileSystem.createDirectory(fullPath + name)

  override fun toString() =
    "VirtualDirectory@$fullPath"

  override fun equals(other: Any?) =
    other is VirtualDirectory && this.fileSystem === other.fileSystem && this.fullPath == other.fullPath

  override fun hashCode() =
    fullPath.hashCode()
}
