package omnia.io

import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.objcPtr
import omnia.data.structure.immutable.ImmutableList
import omnia.platform.swift.asNSString
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileWrapper
import platform.Foundation.NSURL.Companion.URLWithString
import platform.Foundation.lastPathComponent
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.stringByDeletingLastPathComponent
import platform.Foundation.stringByDeletingPathExtension

actual class Directory private constructor(private val path: String) {

  init {
    if (!isDirectory(path)) {
      throw NotADirectoryException(path)
    }
  }

  actual val name: String get() =
    path.asNSString().lastPathComponent.asNSString().stringByDeletingPathExtension

  actual val parentDirectory: Directory?
    get() {
      val parentDirectoryPath = path.asNSString().stringByDeletingLastPathComponent
      return if (isDirectory(parentDirectoryPath)) fromPath(parentDirectoryPath) else null
    }

  actual val parentDirectories: Iterable<Directory> get() {
    val builder = ImmutableList.builder<Directory>()
    var parent = parentDirectory
    while (parent != null) {
      builder.add(parent)
      parent = parent.parentDirectory
    }
    return builder.build()
  }

  actual val files: Iterable<File> get() {
    @Suppress("UNCHECKED_CAST")
    return (fileWrapper.fileWrappers as Map<String, NSFileWrapper>)
        .entries
        .filter { it.value.regularFile }
        .map { File.fromPath(path.asNSString().stringByAppendingPathComponent(it.key)) }
  }

  actual val subdirectories: Iterable<Directory> get() {
    @Suppress("UNCHECKED_CAST")
    return (fileWrapper.fileWrappers as Map<String, NSFileWrapper>)
        .entries
        .filter { it.value.directory }
        .map {fromPath(path.asNSString().stringByAppendingPathComponent(it.key)) }
  }

  private val fileWrapper get() = NSFileWrapper(URLWithString(path)!!, 0, null)

  actual companion object {

    actual val workingDirectory get() = fromPath(NSFileManager.defaultManager.currentDirectoryPath)

    actual val rootDirectory get() = fromPath("/")

    actual fun fromPath(path: String) = Directory(path)

    private fun isDirectory(path: String): Boolean {
      val isDirectory = false
      val exists =
        NSFileManager.defaultManager.fileExistsAtPath(
            path, isDirectory=interpretCPointer(isDirectory.objcPtr()))
      return exists && isDirectory
    }
  }
}