package omnia.io.filesystem.resources

import kotlin.experimental.ExperimentalNativeApi
import kotlinx.cinterop.ExperimentalForeignApi
import omnia.data.cache.Memoized.Companion.memoize
import omnia.io.IOException
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.FileSystem
import omnia.io.filesystem.asAbsolutePath
import omnia.io.filesystem.os.OsFileSystem
import omnia.io.filesystem.sandbox.SandboxDirectory
import omnia.io.filesystem.sandbox.SandboxFile
import omnia.io.filesystem.sandbox.SandboxFileSystem
import platform.Foundation.NSBundle

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
actual class ResourceFileSystem private constructor(private val sandbox: SandboxFileSystem): FileSystem {

  private constructor():
    this(SandboxFileSystem(OsFileSystem.instance, systemResourceDirectory.value, systemResourceDirectory.value))

  actual override val rootDirectory get() =
    ResourceDirectory(this, sandbox.rootDirectory)

  actual override val workingDirectory get() =
    ResourceDirectory(this, sandbox.workingDirectory)

  private fun getResourceDirectory(path: AbsolutePath) =
    sandbox.baseWorkingPath + path.removePrefix(AbsolutePath())

  actual override fun objectExistsAt(path: AbsolutePath) = sandbox.objectExistsAt(path)

  actual override fun directoryExistsAt(path: AbsolutePath) = sandbox.directoryExistsAt(path)

  actual override fun fileExistsAt(path: AbsolutePath) = sandbox.fileExistsAt(path)

  actual override fun getObjectAt(path: AbsolutePath) =
    sandbox.getObjectAt(path)
      .let {
        when (it) {
          is SandboxDirectory -> ResourceDirectory(this, it)
          is SandboxFile -> ResourceFile(this, it)
        }
      }

  actual override fun getDirectoryAt(path: AbsolutePath) =
    ResourceDirectory(this, sandbox.getDirectoryAt(path))

  actual override fun getFileAt(path: AbsolutePath) =
    ResourceFile(this, sandbox.getFileAt(path))

  // TODO add a read-only file system
  actual override fun createDirectoryAt(path: AbsolutePath): ResourceDirectory =
    throw UnsupportedOperationException("Not permitted to create directories in the application resources.")

  // TODO add a read-only file system
  actual override fun createFileAt(path: AbsolutePath): ResourceFile =
    throw UnsupportedOperationException("Not permitted to create files in the application resources.")

  actual companion object {
    private val systemResourceDirectory = memoize {
      OsFileSystem.instance.getDirectoryAt(
        (NSBundle.mainBundle.resourcePath ?: throw IOException("No resource path defined in main bundle"))
          .asAbsolutePath())
    }

    private val memoizedInstance = memoize(::ResourceFileSystem)

    actual val instance = memoizedInstance.value
  }
}
