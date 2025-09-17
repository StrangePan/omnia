package omnia.io.filesystem.resources

import java.io.File as JavaFile
import omnia.data.cache.Memoized.Companion.memoize
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.FileSystem
import omnia.io.filesystem.asAbsolutePath
import omnia.io.filesystem.os.OsDirectory
import omnia.io.filesystem.os.OsFile
import omnia.io.filesystem.os.OsFileSystem

actual class ResourceFileSystem: FileSystem {

  private val osFileSystem = OsFileSystem.instance

  actual override val rootDirectory get() = getDirectoryAt(AbsolutePath())

  actual override val workingDirectory get() = rootDirectory

  private fun getResourceURL(path: AbsolutePath): String? =
    ClassLoader.getSystemResource(path.removePrefix(AbsolutePath()).toString())?.path?.removeSuffix("/")

  private fun getJavaFile(path: AbsolutePath): JavaFile? =
    getResourceURL(path)?.let { JavaFile(it) }

  actual override fun objectExistsAt(path: AbsolutePath) = getResourceURL(path) != null

  actual override fun directoryExistsAt(path: AbsolutePath) = getJavaFile(path)?.isDirectory ?: false

  actual override fun fileExistsAt(path: AbsolutePath) = getJavaFile(path)?.isFile ?: false

  actual override fun getObjectAt(path: AbsolutePath): ResourceFileSystemObject =
    getResourceURL(path)
      ?.let { osFileSystem.getObjectAt(it.asAbsolutePath()) }
      ?.let {
        when (it) {
          is OsDirectory -> ResourceDirectory(this, path, it)
          is OsFile -> ResourceFile(this, path, it)
          else -> null
        }
      } ?: throw FileNotFoundException(path.toString())

  actual override fun getDirectoryAt(path: AbsolutePath): ResourceDirectory =
    getResourceURL(path)?.let { osFileSystem.getDirectoryAt(it.asAbsolutePath()) }
      ?.let { ResourceDirectory(this, path, it) }
      ?: throw FileNotFoundException(path.toString())

  actual override fun getFileAt(path: AbsolutePath): ResourceFile =
    getResourceURL(path)?.let { osFileSystem.getFileAt(it.asAbsolutePath()) }
      ?.let { ResourceFile(this, path, it) }
      ?: throw FileNotFoundException(path.toString())

  // TODO add a read-only file system
  actual override fun createDirectoryAt(path: AbsolutePath): ResourceDirectory =
    throw UnsupportedOperationException("Not permitted to create directories in the application resources.")

  // TODO add a read-only file system
  actual override fun createFileAt(path: AbsolutePath): ResourceFile =
    throw UnsupportedOperationException("Not permitted to create files in the application resources.")

  actual companion object {
    private val memoizedInstance = memoize(::ResourceFileSystem)

    actual val instance = memoizedInstance.value
  }
}
