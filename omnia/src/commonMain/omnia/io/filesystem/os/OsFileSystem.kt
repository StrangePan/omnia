package omnia.io.filesystem.os

import omnia.io.filesystem.FileSystem

expect class OsFileSystem(): FileSystem {

  override val rootDirectory: OsDirectory

  override val workingDirectory: OsDirectory

  override fun isDirectory(path: String): Boolean

  override fun isFile(path: String): Boolean

  override fun getDirectory(path: String): OsDirectory

  override fun getFile(path: String): OsFile

  fun getResource(path: String): OsFile

  override fun createDirectory(path: String): OsDirectory

  override fun createFile(path: String): OsFile
}
