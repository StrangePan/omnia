package omnia.io.filesystem.virtual

import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.FileSystemObject
import omnia.io.filesystem.PathComponent

/**
 * An in-memory virtual directory object. Useful for tests, intermediate migration steps, and other situations where
 * we want to run file-manipulation algorithms without accessing the operating system's storage.
 */
class VirtualDirectory internal constructor(
  internal val fileSystem: VirtualFileSystem,
  internal var fullPathMutable: AbsolutePath):
  VirtualFileSystemObject, Directory {

  override val fullPath: AbsolutePath get() = this.fullPathMutable

  override val name: PathComponent get() =
    fullPath.components.lastOrNull() ?: throw IllegalStateException("Root directory does not have a name")

  override val parentDirectory: VirtualDirectory? get() =
    if (fullPath.isRoot) {
      null
    } else {
      fileSystem.getDirectoryAt(fullPath - 1)
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

  override val contents: Iterable<FileSystemObject> get() =
    fileSystem.getContentsInDirectory(this)

  override val files: Iterable<VirtualFile> get() =
    fileSystem.getFilesInDirectory(this)

  override val subdirectories: Iterable<VirtualDirectory> get() =
    fileSystem.getDirectoriesInDirectory(this)

  override fun createFile(name: PathComponent): VirtualFile =
    fileSystem.createFileAt(fullPath + name)

  override fun createSubdirectory(name: PathComponent): VirtualDirectory =
    fileSystem.createDirectoryAt(fullPath + name)

  override fun toString() =
    "VirtualDirectory@$fullPath"

  override fun equals(other: Any?) =
    other is VirtualDirectory && this.fileSystem === other.fileSystem && this.fullPath == other.fullPath

  override fun hashCode() =
    fullPath.hashCode()

  override fun delete() =
    fileSystem.tree.deleteDirectory(this.fullPath)

  override fun moveTo(path: AbsolutePath) {
    fileSystem.tree.moveDirectory(fullPath, path)
    this.fullPathMutable = path
  }

  override fun copyTo(path: AbsolutePath): VirtualDirectory {
    return fileSystem.tree.copyDirectory(fullPath, path)
  }
}
