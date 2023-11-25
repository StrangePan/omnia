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
  private var listener: ((Event) -> Unit)? = null

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

  /**
   * Registers a file system listener callback that will receive notifications when the file system changes. These
   * notifications are blocking, and can be used to as an opportunity to react to changes or throw errors for tests.
   * Any previous listeners will be overwritten.
   */
  fun setListener(listener: (Event) -> Unit) {
    this.tree.listener = listener
  }

  /** Removes any current listener from the file system. */
  fun clearListener() {
    this.tree.listener = null
  }

  companion object {
    private val ROOT_DIRECTORY_PATH = AbsolutePath()
  }

  sealed interface Event
  sealed interface OnBeforeEvent: Event
  sealed interface OnAfterEvent: Event
  sealed interface CreateFileSystemObject: Event {
    val path: AbsolutePath
  }
  sealed interface OnBeforeCreateFileSystemObject: CreateFileSystemObject, OnBeforeEvent
  data class OnBeforeCreateFile(override val path: AbsolutePath): OnBeforeCreateFileSystemObject
  data class OnBeforeCreateDirectory(override val path: AbsolutePath): OnBeforeCreateFileSystemObject
  sealed interface OnAfterCreateFileSystemObject: CreateFileSystemObject, OnAfterEvent
  data class OnAfterCreateFile(override val path: AbsolutePath): OnAfterCreateFileSystemObject
  data class OnAfterCreateDirectory(override val path: AbsolutePath): OnAfterCreateFileSystemObject
  // TODO add more file system events
}

internal fun AbsolutePath.extractParentDirectoryPaths() =
  this.components
    .scan(AbsolutePath()) { path, component -> path + component }
    .dropLast(1)
