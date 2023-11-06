package omnia.io.filesystem.virtual

import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.FileSystem

/**
 * A virtual, in-memory file system. Useful for tests, intermediate file processing steps, and other tasks that
 * shouldn't touch the operating system's storage.
 */
class VirtualFileSystem(private val workingDirectoryPath: AbsolutePath): FileSystem {

  constructor(): this(ROOT_DIRECTORY_PATH)

  internal val tree = VirtualFileSystemTree()

  init {
    createDirectoryAt(ROOT_DIRECTORY_PATH)
    if (workingDirectoryPath != ROOT_DIRECTORY_PATH) {
      workingDirectoryPath
        .extractParentDirectoryPaths()
        .drop(1) // first is just the root directory
        .plus(workingDirectoryPath)
        .forEach(::createDirectoryAt)
    }
  }

  override val rootDirectory: VirtualDirectory get() =
    getDirectoryAt(AbsolutePath())

  override val workingDirectory: VirtualDirectory get() =
    getDirectoryAt(workingDirectoryPath)

  override fun objectExistsAt(path: AbsolutePath): Boolean =
    tree.getFileSystemObject(path) != null

  override fun directoryExistsAt(path: AbsolutePath): Boolean =
    tree.getDirectory(path) != null

  override fun fileExistsAt(path: AbsolutePath): Boolean =
    tree.getFile(path) != null

  override fun getObjectAt(path: AbsolutePath): VirtualFileSystemObject =
    tree.getFileSystemObject(path) ?: throw FileNotFoundException(path.toString())

  override fun getDirectoryAt(path: AbsolutePath): VirtualDirectory =
    tree.getDirectory(path) ?: throw FileNotFoundException(path.toString())

  override fun getFileAt(path: AbsolutePath): VirtualFile =
    tree.getFile(path) ?: throw FileNotFoundException(path.toString())

  internal fun getContentsInDirectory(directory: VirtualDirectory): ImmutableList<VirtualFileSystemObject> =
    tree.getContentsInDirectory(directory.fullPath)

  internal fun getDirectoriesInDirectory(directory: VirtualDirectory): ImmutableList<VirtualDirectory> =
    tree.getDirectoriesInDirectory(directory.fullPath)

  internal fun getFilesInDirectory(directory: VirtualDirectory): ImmutableList<VirtualFile> =
    tree.getFilesInDirectory(directory.fullPath)

  override fun createDirectoryAt(path: AbsolutePath): VirtualDirectory {
    val directory = VirtualDirectory(this, path)
    if (tree.addDirectory(directory)) {
      return directory
    } else {
      throw FileAlreadyExistsException(tree.getFileSystemObject(path)!!)
    }
  }

  override fun createFileAt(path: AbsolutePath): VirtualFile {
    val file = VirtualFile(this, path)
    if (tree.addFile(file)) {
      return file
    } else {
      throw FileAlreadyExistsException(tree.getFileSystemObject(path)!!)
    }
  }

  companion object {
    private val ROOT_DIRECTORY_PATH = AbsolutePath()
  }
}

internal fun AbsolutePath.extractParentDirectoryPaths() =
  this.components
    .scan(AbsolutePath()) { path, component -> path + component }
    .dropLast(1)
