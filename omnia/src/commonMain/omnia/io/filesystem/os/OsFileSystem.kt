package omnia.io.filesystem.os

import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.FileSystem

expect class OsFileSystem(): FileSystem {

  override val rootDirectory: OsDirectory

  override val workingDirectory: OsDirectory

  override fun objectExistsAt(path: AbsolutePath): Boolean

  override fun isDirectory(path: AbsolutePath): Boolean

  override fun isFile(path: AbsolutePath): Boolean

  override fun getObjectAt(path: AbsolutePath): OsFileSystemObject

  override fun getDirectory(path: AbsolutePath): OsDirectory

  override fun getFile(path: AbsolutePath): OsFile

  fun getResource(path: String): OsFile

  override fun createDirectory(path: AbsolutePath): OsDirectory

  override fun createFile(path: AbsolutePath): OsFile
}
