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
  Directory, SandboxFileSystemObject {

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

  override val contents: Iterable<SandboxFileSystemObject> get() =
    files.plus(subdirectories)

  override val files: Iterable<SandboxFile> get() =
    baseDirectory.files.map { SandboxFile(fileSystem, it) }

  override val subdirectories: Iterable<SandboxDirectory> get() =
    baseDirectory.subdirectories.map { SandboxDirectory(fileSystem, it) }

  override fun createFile(name: PathComponent): SandboxFile =
    SandboxFile(fileSystem, baseDirectory.createFile(name))

  override fun createSubdirectory(name: PathComponent): SandboxDirectory =
    SandboxDirectory(fileSystem, baseDirectory.createSubdirectory(name))

  override fun delete() {
    require(!fullPath.isRoot) { "Cannot delete root directory" }
    require(!fullPath.contains(fileSystem.workingDirectory.fullPath)) { "Cannot delete working directory" }
    baseDirectory.delete()
  }

  override fun moveTo(path: AbsolutePath) {
    require(!fullPath.isRoot) { "Cannot move root directory" }
    require(!path.isRoot) { "Cannot move to root directory" }
    require(!fullPath.contains(fileSystem.workingDirectory.fullPath)) { "Cannot move working directory: $fullPath => $path" }
    baseDirectory.moveTo(fileSystem.toBasePath(path))
  }

  override fun copyTo(path: AbsolutePath): SandboxDirectory {
    require(!path.isRoot) { "Cannot copy to root directory" }
    require(!fullPath.contains(path)) { "Cannot copy directory into itself: $fullPath => $path"}
    return SandboxDirectory(fileSystem, baseDirectory.copyTo(fileSystem.toBasePath(path)))
  }
}
