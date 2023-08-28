package omnia.io.filesystem.virtual

import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.FileSystem

/**
 * A virtual, in-memory file system. Useful for tests, intermediate file processing steps, and other tasks that
 * shouldn't touch the operating system's storage.
 */
class VirtualFileSystem(private val workingDirectoryPath: String): FileSystem {

  constructor(): this("/")

  private val tree = VirtualFileSystemTree()

  init {
    createDirectory("/")
    if (workingDirectoryPath != "/") {
      checkDirectoryPath(workingDirectoryPath)
        .extractParentDirectoryPaths()
        .map(::createDirectory)
    }
  }

  override val rootDirectory: VirtualDirectory get() =
    getDirectory("/")

  override val workingDirectory: VirtualDirectory get() =
    getDirectory(workingDirectoryPath)

  override fun isDirectory(path: String): Boolean =
    tree.getDirectory(path) != null

  override fun isFile(path: String): Boolean =
    tree.getFile(path) != null

  override fun getDirectory(path: String): VirtualDirectory =
    checkDirectoryPath(path).let { tree.getDirectory(it) ?: throw FileNotFoundException(it) }

  override fun getFile(path: String): VirtualFile =
    checkFilePath(path).let { tree.getFile(it) ?: throw FileNotFoundException(it) }

  internal fun getDirectoriesInDirectory(directory: VirtualDirectory): ImmutableList<VirtualDirectory> =
    tree.getDirectoriesInDirectory(directory.fullName + "/")

  internal fun getFilesInDirectory(directory: VirtualDirectory): ImmutableList<VirtualFile> =
    tree.getFilesInDirectory(directory.fullName + "/")

  override fun createDirectory(path: String): VirtualDirectory {
    val directory = checkDirectoryPath(path).let { VirtualDirectory(this, it) }
    if (tree.addDirectory(directory)) {
      return directory
    } else {
      throw FileAlreadyExistsException(tree.getFileSystemObject(path)!!)
    }
  }

  override fun createFile(path: String): VirtualFile {
    val file = checkFilePath(path).let { VirtualFile(this, it) }
    if (tree.addFile(file)) {
      return file
    } else {
      throw FileAlreadyExistsException(tree.getFileSystemObject(path)!!)
    }
  }

  private fun checkDirectoryPath(path: String) =
    path.also {
      require(it.startsWith("/")) { "Directory path must begin with '/': $path" }
      require(it == "/" || !it.endsWith("/")) { "Directory path cannot end with '/': $path" }
    }

  private fun checkFilePath(path: String) =
    path.also {
      require(it.startsWith("/")) { "File path must begin with '/': $path" }
      require(!it.endsWith("/")) { "File path cannot end with '/': $path" }
    }

}

internal fun String.extractParentDirectoryPaths() =
  this.split("/")
    .drop(1)
    .scan("") { previousPath, newComponent -> "$previousPath/$newComponent" }
    .drop(1)
