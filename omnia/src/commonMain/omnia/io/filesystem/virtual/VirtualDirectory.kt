package omnia.io.filesystem.virtual

import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.Directory

/**
 * An in-memory virtual directory object. Useful for tests, intermediate migration steps, and other situations where
 * we want to run file-manipulation algorithms without accessing the operating system's storage.
 */
class VirtualDirectory internal constructor(private val fileSystem: VirtualFileSystem, private val path: String):
  VirtualFileSystemObject, Directory {

  override val name: String get() =
    path.substringAfterLast("/")

  override val fullName: String get() =
    path

  override val parentDirectory: VirtualDirectory? =
    path.substringBeforeLast("/", missingDelimiterValue = "")
      .takeIf(String::isNotEmpty)
      ?.let(fileSystem::getDirectory)

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

  // TODO Add better path parsing and concatenation
  override fun createFile(name: String): VirtualFile =
    fileSystem.createFile("$path/$name")

  // TODO Add better path parsing and concatenation
  override fun createSubdirectory(name: String): VirtualDirectory =
    fileSystem.createDirectory("$path/$name")
}
