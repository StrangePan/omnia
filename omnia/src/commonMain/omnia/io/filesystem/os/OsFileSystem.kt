package omnia.io.filesystem.os

import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.FileSystem
import omnia.io.filesystem.RelativePath

expect class OsFileSystem(): FileSystem {

  override val rootDirectory: OsDirectory

  override val workingDirectory: OsDirectory

  override fun objectExistsAt(path: AbsolutePath): Boolean

  override fun directoryExistsAt(path: AbsolutePath): Boolean

  override fun fileExistsAt(path: AbsolutePath): Boolean

  override fun getObjectAt(path: AbsolutePath): OsFileSystemObject

  override fun getDirectoryAt(path: AbsolutePath): OsDirectory

  override fun getFileAt(path: AbsolutePath): OsFile

  fun getResourceAt(path: RelativePath): OsFile

  override fun createDirectoryAt(path: AbsolutePath): OsDirectory

  override fun createFileAt(path: AbsolutePath): OsFile
}
