package omnia.io.filesystem.virtual

import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.data.structure.mutable.HashMap
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.FileNotFoundException

/**
 * A helper data structure for storing, tracking, and querying virtual files and directories. Designed to separate the
 * nitty-gritty data structure maintenance details from the higher-leve file system logic.
 */
internal class VirtualFileSystemTree {

  private val directories = HashMap.create<AbsolutePath, VirtualDirectory>()
  private val files = HashMap.create<AbsolutePath, VirtualFile>()

  fun getDirectory(path: AbsolutePath) =
    directories.valueOf(path)

  fun getFile(path: AbsolutePath) =
    files.valueOf(path)

  fun getFileSystemObject(path: AbsolutePath): VirtualFileSystemObject? =
    getDirectory(path) ?: getFile(path)

  fun addDirectory(directory: VirtualDirectory): Boolean =
    if (directories.valueOf(directory.fullPath) == null && files.valueOf(directory.fullPath) == null) {
      directory.fullPath
        .extractParentDirectoryPaths()
        .forEach { parentDirectoryPath ->
          if (getDirectory(parentDirectoryPath) == null) {
            throw FileNotFoundException(
              "Cannot create directory '${directory.fullPath}' when missing parent directory '$parentDirectoryPath' " +
                "does not exist.")
          }
        }
      directories.putMapping(directory.fullPath, directory)
      true
    } else {
      false
    }

  fun addFile(file: VirtualFile): Boolean =
    if (directories.valueOf(file.fullPath) == null && files.valueOf(file.fullPath) == null) {
      file.fullPath
        .extractParentDirectoryPaths()
        .forEach { parentDirectoryPath ->
          if (getDirectory(parentDirectoryPath) == null) {
            throw FileNotFoundException(
              "Cannot create directory '${file.fullPath}' when missing parent directory '$parentDirectoryPath' does " +
                "not exist.")
          }
        }
      files.putMapping(file.fullPath, file)
      true
    } else {
      false
    }

  fun getFilesInDirectory(path: AbsolutePath): ImmutableList<VirtualFile> =
    files.entries
      .filter { (it.key.components.count - 1) == path.components.count && (it.key - 1) == path }
      .map { it.value }
      .sortedBy { it.name.name }
      .toImmutableList()

  fun getDirectoriesInDirectory(path: AbsolutePath): ImmutableList<VirtualDirectory> =
    directories.entries
      .filter { (it.key.components.count - 1) == path.components.count && (it.key - 1) == path }
      .map { it.value }
      .sortedBy { it.name.name }
      .toImmutableList()
}
