package omnia.io.filesystem.virtual

import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.data.structure.mutable.HashMap
import omnia.io.filesystem.Directory
import omnia.io.filesystem.File
import omnia.io.filesystem.FileNotFoundException

/**
 * A helper data structure for storing, tracking, and querying virtual files and directories. Designed to separate the
 * nitty-gritty data structure maintenance details from the higher-leve file system logic.
 */
internal class VirtualFileSystemTree {

  private val directories = HashMap.create<String, VirtualDirectory>()
  private val files = HashMap.create<String, VirtualFile>()

  fun getDirectory(path: String) =
    directories.valueOf(path)

  fun getFile(path: String) =
    files.valueOf(path)

  fun getFileSystemObject(path: String): VirtualFileSystemObject? =
    getDirectory(path) ?: getFile(path)

  fun addDirectory(directory: VirtualDirectory): Boolean =
    if (directories.valueOf(directory.fullName) == null && files.valueOf(directory.fullName) == null) {
      directory.fullName
        .extractParentDirectoryPaths()
        .dropLast(1)
        .forEach { parentDirectoryPath ->
          if (getDirectory(parentDirectoryPath) == null) {
            throw FileNotFoundException(
              "Cannot create directory '${directory.fullName}' when missing parent directory '$parentDirectoryPath' " +
                "does not exist.")
          }
        }
      directories.putMapping(directory.fullName, directory)
      true
    } else {
      false
    }

  fun addFile(file: VirtualFile): Boolean =
    if (directories.valueOf(file.fullName) == null && files.valueOf(file.fullName) == null) {
      file.fullName
        .extractParentDirectoryPaths()
        .dropLast(1)
        .forEach { parentDirectoryPath ->
          if (getDirectory(parentDirectoryPath) == null) {
            throw FileNotFoundException(
              "Cannot create directory '${file.fullName}' when missing parent directory '$parentDirectoryPath' does " +
                "not exist.")
          }
        }
      files.putMapping(file.fullName, file)
      true
    } else {
      false
    }

  fun getFilesInDirectory(path: String): ImmutableList<VirtualFile> =
    files.entries
      .filter { it.key.startsWith(path) && !it.key.substring(path.length).contains("/") }
      .map { it.value }
      .sortedBy(File::name)
      .toImmutableList()

  fun getDirectoriesInDirectory(path: String): ImmutableList<VirtualDirectory> =
    directories.entries
      .filter { it.key.startsWith(path) && !it.key.substring(path.length).contains("/") }
      .map { it.value }
      .sortedBy(Directory::name)
      .toImmutableList()
}
