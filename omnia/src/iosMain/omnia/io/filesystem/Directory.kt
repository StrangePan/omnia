package omnia.io.filesystem

import omnia.data.structure.immutable.ImmutableList
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
        .map { fromPath(path.asNSString().stringByAppendingPathComponent(it.key)) }
  }

  private val fileWrapper get() = NSFileWrapper(fileURLWithPath(path), 0, null)

  actual fun createFile(name: String): File {
    files.firstOrNull { it.name == name }?.let { throw FileAlreadyExistsException(it) }
    return "$path/$name"
        .also { assert(NSFileManager.defaultManager.createFileAtPath("$path/$name", null, emptyMap<Any?, Any?>())) }
        .let { File.fromPath(it) }
  }

  actual fun createSubdirectory(name: String): Directory {
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

    actual fun fromPath(path: String) = Directory(path)

    private fun isDirectory(path: String): Boolean {
      return getFileInfo(path).let { it.exists && it.isDirectory }
    }
  }
}
