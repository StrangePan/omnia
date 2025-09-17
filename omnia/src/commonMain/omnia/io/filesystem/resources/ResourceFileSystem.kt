package omnia.io.filesystem.resources

import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.FileSystem

expect class ResourceFileSystem: FileSystem {

  override val rootDirectory: ResourceDirectory

  override val workingDirectory: ResourceDirectory

  override fun objectExistsAt(path: AbsolutePath): Boolean

  override fun directoryExistsAt(path: AbsolutePath): Boolean

  override fun fileExistsAt(path: AbsolutePath): Boolean

  override fun getObjectAt(path: AbsolutePath): ResourceFileSystemObject

  override fun getDirectoryAt(path: AbsolutePath): ResourceDirectory

  override fun getFileAt(path: AbsolutePath): ResourceFile

  override fun createDirectoryAt(path: AbsolutePath): ResourceDirectory

  override fun createFileAt(path: AbsolutePath): ResourceFile

  companion object {
    val instance: ResourceFileSystem
  }
}
