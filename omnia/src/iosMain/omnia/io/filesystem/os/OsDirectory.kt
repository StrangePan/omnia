package omnia.io.filesystem.os

import kotlinx.cinterop.ExperimentalForeignApi
import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.NotADirectoryException
import omnia.io.filesystem.PathComponent
import omnia.io.filesystem.asPathComponent
import platform.Foundation.NSFileWrapper
import platform.Foundation.NSURL.Companion.fileURLWithPath

@OptIn(ExperimentalForeignApi::class)
actual class OsDirectory internal constructor(internal val fileSystem: OsFileSystem, actual override val fullPath: AbsolutePath): Directory {

  init {
    if (!fileSystem.isDirectory(fullPath)) {
      throw NotADirectoryException(fullPath.toString())
    }
  }

  actual override val name get() =
    fullPath.components.last()

  actual override val parentDirectory: OsDirectory? get() {
    if (fullPath.isRoot) {
      return null
    }
    val parentDirectoryPath = fullPath - 1
    return if (fileSystem.isDirectory(parentDirectoryPath)) OsDirectory(fileSystem, parentDirectoryPath) else null
  }

  actual override val parentDirectories: Iterable<OsDirectory> get() {
    val builder = ImmutableList.builder<OsDirectory>()
    var parent = parentDirectory
    while (parent != null) {
      builder.add(parent)
      parent = parent.parentDirectory
    }
    return builder.build()
  }

  actual override val files: Iterable<OsFile> get() {
    @Suppress("UNCHECKED_CAST")
    return (fileWrapper.fileWrappers as Map<String, NSFileWrapper>)
        .entries
        .filter { it.value.regularFile }
        .map { OsFile(fileSystem, fullPath + it.key.asPathComponent()) }
  }

  actual override val subdirectories: Iterable<OsDirectory> get() {
    @Suppress("UNCHECKED_CAST")
    return (fileWrapper.fileWrappers as Map<String, NSFileWrapper>)
        .entries
        .filter { it.value.directory }
        .map { OsDirectory(fileSystem, fullPath + it.key.asPathComponent()) }
  }

  private val fileWrapper get() = NSFileWrapper(fileURLWithPath(fullPath.toString()), 0u, null)

  actual override fun createFile(name: PathComponent): OsFile =
    fileSystem.createFile(fullPath + name)

  actual override fun createSubdirectory(name: PathComponent): OsDirectory =
    fileSystem.createDirectory(fullPath + name)
}
