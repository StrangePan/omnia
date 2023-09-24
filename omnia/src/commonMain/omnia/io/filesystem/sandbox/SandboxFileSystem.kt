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

  override fun isFile(path: AbsolutePath): Boolean =
    baseFileSystem.isFile(toBasePath(path))

  override fun getDirectory(path: AbsolutePath): SandboxDirectory =
    SandboxDirectory(this, baseFileSystem.getDirectory(toBasePath(path)))

  override fun getFile(path: AbsolutePath): SandboxFile =
    SandboxFile(this, baseFileSystem.getFile(toBasePath(path)))

  override fun createDirectory(path: AbsolutePath): SandboxDirectory =
    SandboxDirectory(this, baseFileSystem.createDirectory(toBasePath(path)))

  override fun createFile(path: AbsolutePath): SandboxFile =
    SandboxFile(this, baseFileSystem.createFile(toBasePath(path)))

  internal fun toBasePath(sandboxPath: AbsolutePath): AbsolutePath =
    AbsolutePath((baseRootPath.components + sandboxPath.components).toImmutableList())

  override fun isDirectory(path: AbsolutePath): Boolean =
    baseFileSystem.isDirectory(toBasePath(path))
}


