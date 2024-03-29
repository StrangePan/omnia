package omnia.io.filesystem.os

import kotlin.experimental.ExperimentalNativeApi
import kotlinx.cinterop.ExperimentalForeignApi
import omnia.data.cache.Memoized.Companion.memoize
import omnia.io.IOException
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.FileSystem
import omnia.io.filesystem.asAbsolutePath
import platform.Foundation.NSBundle
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
actual class OsFileSystem private constructor(): FileSystem {

  actual override val rootDirectory get() =
    OsDirectory(this, AbsolutePath())

  actual override val workingDirectory get() =
    NSFileManager.defaultManager
      .URLForDirectory(NSDocumentDirectory, NSUserDomainMask, null, true, null)
      .let { it?.path!! }
      .let { OsDirectory(this, it.asAbsolutePath()) }

  actual override fun objectExistsAt(path: AbsolutePath): Boolean =
    getFileInfo(path).exists

  actual override fun directoryExistsAt(path: AbsolutePath) =
    getFileInfo(path).let { it.exists && it.isDirectory }

  actual override fun fileExistsAt(path: AbsolutePath) =
    getFileInfo(path).let { it.exists && !it.isDirectory }

  actual override fun getObjectAt(path: AbsolutePath) =
    if (directoryExistsAt(path)) {
      getDirectoryAt(path)
    } else {
      getFileAt(path)
    }

  actual override fun getDirectoryAt(path: AbsolutePath) =
    OsDirectory(this, path)

  actual override fun getFileAt(path: AbsolutePath) =
    OsFile(this, path)

  private fun getResourcePath(path: AbsolutePath) =
    (NSBundle.mainBundle.resourcePath ?: throw IOException("No resource path defined in main bundle"))
      .asAbsolutePath() + path.removePrefix(AbsolutePath.empty())

  actual fun getResourceFileAt(path: AbsolutePath): OsFile =
    OsFile(this, getResourcePath(path))

  actual fun getResourceDirectoryAt(path: AbsolutePath): OsDirectory =
    OsDirectory(this, getResourcePath(path))

  actual fun getResourceObjectAt(path: AbsolutePath): OsFileSystemObject =
    getFileSystemObject(getResourcePath(path)) ?: throw FileNotFoundException(getResourcePath(path).toString())

  internal fun getFileSystemObject(path: AbsolutePath): OsFileSystemObject? =
    getFileInfo(path).let { info ->
      if (!info.exists) {
        null
      } else if (info.isDirectory) {
        OsDirectory(this, path)
      } else {
        OsFile(this, path)
      }
    }

  actual override fun createDirectoryAt(path: AbsolutePath): OsDirectory =
    path
      .also {
        getFileSystemObject(it)?.let { fsObject -> throw FileAlreadyExistsException(fsObject) }
      }
      .also {
        assert(NSFileManager.defaultManager.createDirectoryAtPath(it.toString(), emptyMap<Any?, Any?>()))
      }
      .let { OsDirectory(this, it) }

  actual override fun createFileAt(path: AbsolutePath): OsFile =
    path
      .also {
        getFileSystemObject(it)?.let { fsObject -> throw FileAlreadyExistsException(fsObject) }
      }
      .also {
        assert(NSFileManager.defaultManager.createFileAtPath(it.toString(), null, emptyMap<Any?, Any?>()))
      }
      .let { OsFile(this, it) }

  actual companion object {
    private val memoizedInstance = memoize(::OsFileSystem)

    actual val instance get() = memoizedInstance.value
  }
}
