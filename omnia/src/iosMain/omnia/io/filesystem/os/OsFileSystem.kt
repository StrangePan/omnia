package omnia.io.filesystem.os

import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileSystem
import omnia.io.filesystem.FileSystemObject
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class OsFileSystem actual constructor(): FileSystem {

  actual override val rootDirectory get() =
    OsDirectory(this, "/")

  actual override val workingDirectory get() =
    NSFileManager.defaultManager
      .URLForDirectory(NSDocumentDirectory, NSUserDomainMask, null, true, null)
      .let { it?.path!! }
      .let { OsDirectory(this, it) }

  actual override fun isDirectory(path: String) =
    getFileInfo(path).let { it.exists && it.isDirectory }

  actual override fun isFile(path: String) =
    getFileInfo(path).let { it.exists && !it.isDirectory }

  actual override fun getDirectory(path: String) =
    OsDirectory(this, path)

  actual override fun getFile(path: String) =
    OsFile(this, path)

  actual fun getResource(path: String): OsFile =
    TODO("not implemented")

  internal fun getFileSystemObject(path: String): FileSystemObject? =
    getFileInfo(path).let { info ->
      if (!info.exists) {
        null
      } else if (info.isDirectory) {
        OsDirectory(this, path)
      } else {
        OsFile(this, path)
      }
    }

  actual override fun createDirectory(path: String): OsDirectory =
    path
      .also {
        getFileSystemObject(it)?.let { fsObject -> throw FileAlreadyExistsException(fsObject) }
      }
      .also {
        assert(NSFileManager.defaultManager.createDirectoryAtPath(it, emptyMap<Any?, Any?>()))
      }
      .let { OsDirectory(this, it) }

  actual override fun createFile(path: String): OsFile =
    path
      .also {
        getFileSystemObject(it)?.let { fsObject -> throw FileAlreadyExistsException(fsObject) }
      }
      .also {
        assert(NSFileManager.defaultManager.createFileAtPath(it, null, emptyMap<Any?, Any?>()))
      }
      .let { OsFile(this, it) }
}
