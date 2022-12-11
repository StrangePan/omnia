package omnia.io

import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.objcPtr
import omnia.data.structure.immutable.ImmutableList
import omnia.platform.swift.asNSString
import platform.Foundation.*
import platform.Foundation.NSURL.Companion.URLWithString

actual class Directory private constructor(private val path: String): FileSystemObject {

  init {
    if (!isDirectory(path)) {
      throw NotADirectoryException(path)
    }
  }

  actual override val name get() =
    path.asNSString().lastPathComponent.asNSString().stringByDeletingPathExtension

  actual override val fullName get() = path

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

  actual fun createFile(name: String): File {
    files.firstOrNull { it.name == name }?.let { throw FileAlreadyExistsException(it) }
    return fileWrapper.addRegularFileWithContents(NSData(), name)
        .also { assert(it == name) }
        .let { File.fromPath(it) }
  }

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