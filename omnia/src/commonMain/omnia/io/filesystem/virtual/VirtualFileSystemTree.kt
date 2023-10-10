package omnia.io.filesystem.virtual

import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.data.structure.mutable.ArrayList
import omnia.data.structure.mutable.HashMap
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.NotADirectoryException
import omnia.io.filesystem.NotAFileException

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

  fun deleteFile(path: AbsolutePath) {
    val file = files.removeKey(path)
    if (file == null) {
      if (directories.valueOf(path) != null) {
        throw NotAFileException(path.toString())
      }
      throw FileNotFoundException(path.toString())
    }
  }

  fun deleteDirectory(path: AbsolutePath) {
    val directory = directories.removeKey(path)
    if (directory == null) {
      if (files.valueOf(path) != null) {
        throw NotADirectoryException(path.toString())
      }
      throw FileNotFoundException(path.toString())
    }
    this.directories.keys
      .filter { path.contains(it) }
      .forEach { this.directories.removeKey(it) }
    this.files.keys
      .filter { path.contains(it) }
      .forEach { this.files.removeKey(it) }
  }

  fun moveFile(from: AbsolutePath, to: AbsolutePath) {
    require(!from.isRoot) { "Cannot move the root directory"}
    require(!to.isRoot) { "Cannot move the root directory"}
    val file = files.valueOf(from)
    if (file == null) {
      if (directories.valueOf(from) != null) {
        throw NotAFileException(from.toString())
      }
      throw FileNotFoundException(from.toString())
    }
    files.valueOf(to)?.let { throw FileAlreadyExistsException(it) }
    directories.valueOf(to)?.let { throw FileAlreadyExistsException(it) }
    val parentDirectory = to - 1
    if (directories.valueOf(parentDirectory) == null) {
      throw FileNotFoundException(parentDirectory.toString())
    }

    files.putMapping(to, file)
    files.removeKey(from)
    file.fullPathMutable = to
  }

  fun moveDirectory(from: AbsolutePath, to: AbsolutePath) {
    require(!from.isRoot) { "Cannot move the root directory"}
    require(!to.isRoot) { "Cannot move the root directory"}
    require(!from.contains(to)) { "Cannot move a directory into itself: $from => $to" }
    val directory = getFileSystemObject(from) ?: throw FileNotFoundException(from.toString())
    if (directory !is Directory) {
      throw NotADirectoryException(from.toString())
    }
    files.valueOf(to)?.let { throw FileAlreadyExistsException(it) }
    directories.valueOf(to)?.let { throw FileAlreadyExistsException(it) }

    files.entries
      .filter { from.contains(it.key) }
      .toImmutableList()
      .forEach { entry ->
        val newPath = entry.key.replacePrefix(from, to)
        files.removeKey(entry.key)
        files.putMapping(newPath, entry.value)
        entry.value.fullPathMutable = newPath
      }
    directories.entries
      .filter { from.contains(it.key) }
      .toImmutableList()
      .forEach { entry ->
        val newPath = entry.key.replacePrefix(from, to)
        directories.removeKey(entry.key)
        directories.putMapping(newPath, entry.value)
        entry.value.fullPathMutable = newPath
      }
  }

  fun copyFile(from: AbsolutePath, to: AbsolutePath): VirtualFile {
    require(!from.isRoot) { "Cannot copy the root directory" }
    require(!to.isRoot) { "Cannot copy the root directory" }
    val file = files.valueOf(from)
    if (file == null) {
      if (directories.valueOf(from) != null) {
        throw NotAFileException(from.toString())
      }
      throw FileNotFoundException(from.toString())
    }
    files.valueOf(to)?.let { throw FileAlreadyExistsException(it) }
    directories.valueOf(to)?.let { throw FileAlreadyExistsException(it) }
    val parentDirectory = to - 1
    if (directories.valueOf(parentDirectory) == null) {
      throw FileNotFoundException(parentDirectory.toString())
    }

    val newFile = VirtualFile(file.fileSystem, to, ArrayList.copyOf(file.lines))
    files.putMapping(to, newFile)
    return newFile
  }

  fun copyDirectory(from: AbsolutePath, to: AbsolutePath): VirtualDirectory {
    require(!from.isRoot) { "Cannot copy the root directory" }
    require(!to.isRoot) { "Cannot copy the root directory" }
    require(!from.contains(to)) { "Cannot copy a directory into itself: $from => $to" }
    val fromDirectory = getFileSystemObject(from) ?: throw FileNotFoundException(from.toString())
    if (fromDirectory !is Directory) {
      throw NotADirectoryException(from.toString())
    }
    getFileSystemObject(to)?.let { throw FileAlreadyExistsException(it) }

    directories.entries
      .filter { from.contains(it.key) }
      .toImmutableList()
      .forEach { entry ->
        val newPath = entry.key.replacePrefix(from, to)
        directories.putMapping(newPath, VirtualDirectory(entry.value.fileSystem, newPath))
      }

    files.entries
      .filter { from.contains(it.key) }
      .toImmutableList()
      .forEach { entry ->
        val newPath = entry.key.replacePrefix(from, to)
        files.putMapping(newPath, VirtualFile(entry.value.fileSystem, newPath, ArrayList.copyOf(entry.value.lines)))
      }

    return directories.valueOf(to)!!
  }
}
