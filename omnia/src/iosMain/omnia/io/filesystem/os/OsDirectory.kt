package omnia.io.filesystem.os

import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.Directory
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.NotADirectoryException
import omnia.platform.swift.asNSString
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileWrapper
import platform.Foundation.NSURL.Companion.fileURLWithPath
import platform.Foundation.NSUserDomainMask
import platform.Foundation.lastPathComponent
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.stringByDeletingLastPathComponent
import platform.Foundation.stringByDeletingPathExtension

actual class OsDirectory private constructor(private val path: String): Directory {

  init {
    if (!isDirectory(path)) {
      throw NotADirectoryException(path)
    }
  }

  actual override val name get() =
    path.asNSString().lastPathComponent.asNSString().stringByDeletingPathExtension

  actual override val fullName get() = path

  actual override val parentDirectory: OsDirectory?
    get() {
      val parentDirectoryPath = path.asNSString().stringByDeletingLastPathComponent
      return if (isDirectory(parentDirectoryPath)) fromPath(parentDirectoryPath) else null
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
        .map { OsFile.fromPath(path.asNSString().stringByAppendingPathComponent(it.key)) }
  }

  actual override val subdirectories: Iterable<OsDirectory> get() {
    @Suppress("UNCHECKED_CAST")
    return (fileWrapper.fileWrappers as Map<String, NSFileWrapper>)
        .entries
        .filter { it.value.directory }
        .map { fromPath(path.asNSString().stringByAppendingPathComponent(it.key)) }
  }

  private val fileWrapper get() = NSFileWrapper(fileURLWithPath(path), 0, null)

  actual override fun createFile(name: String): OsFile {
    files.firstOrNull { it.name == name }?.let { throw FileAlreadyExistsException(it) }
    return "$path/$name"
        .also { assert(NSFileManager.defaultManager.createFileAtPath("$path/$name", null, emptyMap<Any?, Any?>())) }
        .let { OsFile.fromPath(it) }
  }

  actual override fun createSubdirectory(name: String): OsDirectory {
    files.firstOrNull { it.name == name }?.let { throw FileAlreadyExistsException(it) }
    return "$path/$name"
      .also { assert(NSFileManager.defaultManager.createDirectoryAtPath(it, emptyMap<Any?, Any?>())) }
      .let { fromPath(it) }
  }

  actual companion object {

    actual val workingDirectory get() =
      NSFileManager.defaultManager.URLForDirectory(
        NSDocumentDirectory, NSUserDomainMask, null, true, null)
        .let { it?.path!! }
        .let { fromPath(it) }

    actual val rootDirectory get() = fromPath("/")

    actual fun fromPath(path: String) = OsDirectory(path)

    private fun isDirectory(path: String): Boolean {
      return getFileInfo(path).let { it.exists && it.isDirectory }
    }
  }
}
