package omnia.io.filesystem.os

import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.Directory
import omnia.io.filesystem.NotADirectoryException
import omnia.platform.swift.asNSString
import platform.Foundation.NSFileWrapper
import platform.Foundation.NSURL.Companion.fileURLWithPath
import platform.Foundation.lastPathComponent
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.stringByDeletingLastPathComponent
import platform.Foundation.stringByDeletingPathExtension

actual class OsDirectory internal constructor(internal val fileSystem: OsFileSystem, private val path: String): Directory {

  init {
    if (!fileSystem.isDirectory(path)) {
      throw NotADirectoryException(path)
    }
  }

  actual override val name get() =
    path.asNSString().lastPathComponent.asNSString().stringByDeletingPathExtension

  actual override val fullName get() = path

  actual override val parentDirectory: OsDirectory?
    get() {
      val parentDirectoryPath = path.asNSString().stringByDeletingLastPathComponent
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
        .map { OsFile(fileSystem, path.asNSString().stringByAppendingPathComponent(it.key)) }
  }

  actual override val subdirectories: Iterable<OsDirectory> get() {
    @Suppress("UNCHECKED_CAST")
    return (fileWrapper.fileWrappers as Map<String, NSFileWrapper>)
        .entries
        .filter { it.value.directory }
        .map { OsDirectory(fileSystem, path.asNSString().stringByAppendingPathComponent(it.key)) }
  }

  private val fileWrapper get() = NSFileWrapper(fileURLWithPath(path), 0u, null)

  actual override fun createFile(name: String): OsFile =
    fileSystem.createFile("$path/$name")

  actual override fun createSubdirectory(name: String): OsDirectory =
    fileSystem.createDirectory("$path/$name")
}
