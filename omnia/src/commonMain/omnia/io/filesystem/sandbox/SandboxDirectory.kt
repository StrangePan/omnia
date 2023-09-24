package omnia.io.filesystem.sandbox

import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.PathComponent

/**
 * A [Directory] wrapper that prevents access to directories outside its parent [SandboxFileSystem].
 */
class SandboxDirectory internal constructor(
  private val fileSystem: SandboxFileSystem,
  private val baseDirectory: Directory):
  Directory {

  init {
    require(fileSystem.baseRootPath.contains(baseDirectory.fullPath))
  }

  override val fullPath: AbsolutePath get() =
    fileSystem.toSandboxPath(baseDirectory.fullPath)

  override val name: PathComponent get() =
    fullPath.components.last()

  override val parentDirectory: SandboxDirectory? get() =
    if (fullPath.components.isPopulated) {
      fileSystem.getDirectory(fullPath - 1)
    } else {
      null
    }

  override val parentDirectories: Iterable<SandboxDirectory> get() =
    baseDirectory.parentDirectories.takeWhile { fileSystem.baseRootPath.contains(it.fullPath) }
      .map { SandboxDirectory(fileSystem, it) }

  override val files: Iterable<SandboxFile> get() =
    baseDirectory.files.map { SandboxFile(fileSystem, it) }


  override val subdirectories: Iterable<SandboxDirectory> get() =
    baseDirectory.subdirectories.map { SandboxDirectory(fileSystem, it) }

  override fun createFile(name: PathComponent): SandboxFile =
    SandboxFile(fileSystem, baseDirectory.createFile(name))

  override fun createSubdirectory(name: PathComponent): SandboxDirectory =
    SandboxDirectory(fileSystem, baseDirectory.createSubdirectory(name))

}
