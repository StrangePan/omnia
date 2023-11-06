package omnia.io.filesystem.sandbox

import omnia.data.structure.immutable.ImmutableList.Companion.toImmutableList
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.FileSystem

/**
 * A lightweight [FileSystem] wrapper that can only operate within a specific subdirectory of another filesystem.
 */
class SandboxFileSystem(
  internal val baseFileSystem: FileSystem,
  baseRootDirectory: Directory,
  baseWorkingDirectory: Directory):
  FileSystem {

  internal val baseRootPath = baseRootDirectory.fullPath
  internal val baseWorkingPath = baseWorkingDirectory.fullPath

  init {
    require(baseRootPath.contains(baseWorkingPath))
  }

  override val rootDirectory: SandboxDirectory =
    SandboxDirectory(this, baseRootDirectory)

  override val workingDirectory: SandboxDirectory =
    SandboxDirectory(this, baseWorkingDirectory)

  internal fun toSandboxPath(basePath: AbsolutePath): AbsolutePath {
    require(this.baseRootPath.contains(basePath))
    return AbsolutePath(basePath.components.drop(baseRootPath.components.count).toImmutableList())
  }

  override fun objectExistsAt(path: AbsolutePath): Boolean =
    baseFileSystem.objectExistsAt(toBasePath(path))

  override fun fileExistsAt(path: AbsolutePath): Boolean =
    baseFileSystem.fileExistsAt(toBasePath(path))

  override fun directoryExistsAt(path: AbsolutePath): Boolean =
    baseFileSystem.directoryExistsAt(toBasePath(path))

  override fun getObjectAt(path: AbsolutePath): SandboxFileSystemObject =
    if (fileExistsAt(path)) {
      getFileAt(path)
    } else {
      getDirectoryAt(path)
    }

  override fun getDirectoryAt(path: AbsolutePath): SandboxDirectory =
    SandboxDirectory(this, baseFileSystem.getDirectoryAt(toBasePath(path)))

  override fun getFileAt(path: AbsolutePath): SandboxFile =
    SandboxFile(this, baseFileSystem.getFileAt(toBasePath(path)))

  override fun createDirectoryAt(path: AbsolutePath): SandboxDirectory =
    SandboxDirectory(this, baseFileSystem.createDirectoryAt(toBasePath(path)))

  override fun createFileAt(path: AbsolutePath): SandboxFile =
    SandboxFile(this, baseFileSystem.createFileAt(toBasePath(path)))

  internal fun toBasePath(sandboxPath: AbsolutePath): AbsolutePath =
    AbsolutePath((baseRootPath.components + sandboxPath.components).toImmutableList())
}


