package omnia.io.filesystem.virtual

import omnia.data.structure.Map
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
import omnia.io.filesystem.virtual.VirtualFileSystem.OnAfterCreateDirectory
import omnia.io.filesystem.virtual.VirtualFileSystem.OnAfterCreateFile
import omnia.io.filesystem.virtual.VirtualFileSystem.OnAfterMoveDirectory
import omnia.io.filesystem.virtual.VirtualFileSystem.OnAfterMoveFile
import omnia.io.filesystem.virtual.VirtualFileSystem.OnBeforeCreateDirectory
import omnia.io.filesystem.virtual.VirtualFileSystem.OnBeforeCreateFile
import omnia.io.filesystem.virtual.VirtualFileSystem.OnBeforeMoveDirectory
import omnia.io.filesystem.virtual.VirtualFileSystem.OnBeforeMoveFile

/**
 * A helper data structure for storing, tracking, and querying virtual files and directories. Designed to separate the
 * nitty-gritty data structure maintenance details from the higher-leve file system logic.
 */
internal class VirtualFileSystemTree {

  private val directories = HashMap.create<AbsolutePath, VirtualDirectory>()
  private val files = HashMap.create<AbsolutePath, VirtualFile>()
  internal var listener: ((VirtualFileSystem.Event) -> Unit)? = null

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
      emit(OnBeforeCreateDirectory(directory.fullPath))
      directories.putMapping(directory.fullPath, directory)
      emit(OnAfterCreateDirectory(directory.fullPath))
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
              "Cannot create file '${file.fullPath}' when missing parent directory '$parentDirectoryPath' does " +
                "not exist.")
          }
        }
      emit(OnBeforeCreateFile(file.fullPath))
      files.putMapping(file.fullPath, file)
      emit(OnAfterCreateFile(file.fullPath))
      true
    } else {
      false
    }

  fun getContentsInDirectory(path: AbsolutePath): ImmutableList<VirtualFileSystemObject> =
    files.entries.plus(directories.entries).filterAndSort(path)

  fun getFilesInDirectory(path: AbsolutePath): ImmutableList<VirtualFile> =
    files.entries.filterAndSort(path)

  fun getDirectoriesInDirectory(path: AbsolutePath): ImmutableList<VirtualDirectory> =
    directories.entries.filterAndSort(path)

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

    emit(OnBeforeMoveFile(from, to))
    files.putMapping(to, file)
    files.removeKey(from)
    emit(OnAfterMoveFile(from, to))
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

    files.keys.filter(from::contains)
      .map { OnBeforeMoveFile(it, it.replacePrefix(from, to)) }
      .forEach(::emit)
    directories.keys.filter(from::contains)
      .map { OnBeforeMoveDirectory(it, it.replacePrefix(from, to)) }
      .forEach(::emit)

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

    files.keys.filter(to::contains)
      .map { OnAfterMoveFile(it.replacePrefix(to, from), it) }
      .forEach(::emit)
    directories.keys.filter(to::contains)
      .map { OnAfterMoveDirectory(it.replacePrefix(to, from), it) }
      .forEach(::emit)
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
    emit(OnBeforeCreateFile(newFile.fullPath))
    files.putMapping(to, newFile)
    emit(OnAfterCreateFile(newFile.fullPath))
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
        emit(OnBeforeCreateDirectory(newPath))
        directories.putMapping(newPath, VirtualDirectory(entry.value.fileSystem, newPath))
        emit(OnAfterCreateDirectory(newPath))
      }

    files.entries
      .filter { from.contains(it.key) }
      .toImmutableList()
      .forEach { entry ->
        val newPath = entry.key.replacePrefix(from, to)
        emit(OnBeforeCreateFile(newPath))
        files.putMapping(newPath, VirtualFile(entry.value.fileSystem, newPath, ArrayList.copyOf(entry.value.lines)))
        emit(OnAfterCreateFile(newPath))
      }

    return directories.valueOf(to)!!
  }

  private fun emit(event: VirtualFileSystem.Event) {
    listener?.invoke(event)
  }
}

private fun <ValueType: VirtualFileSystemObject> Iterable<Map.Entry<AbsolutePath, ValueType>>.filterAndSort(path: AbsolutePath): ImmutableList<ValueType> =
  this.filter { (it.key.components.count - 1) == path.components.count && (it.key - 1) == path }
    .map { it.value }
    .sortedBy { it.name.name }
    .toImmutableList()
