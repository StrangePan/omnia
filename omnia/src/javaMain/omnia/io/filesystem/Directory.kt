package omnia.io.filesystem

import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.immutable.ImmutableList.Companion
import omnia.io.IOException

actual class Directory private constructor(private val jFile: java.io.File): FileSystemObject {

  init {
    if (!jFile.isDirectory) {
      throw NotADirectoryException(jFile.absolutePath)
    }
  }

  actual override val name: String get() = jFile.absoluteFile.name

  actual override val fullName: String get() = jFile.absolutePath

  actual val parentDirectory: Directory?
    get() = jFile.absoluteFile.parentFile?.let(Directory::fromJFile)

  actual val parentDirectories: Iterable<Directory> get() {
    val builder = ImmutableList.builder<Directory>()
    var parent = parentDirectory
    while (parent != null) {
      builder.add(parent)
      parent = parent.parentDirectory
    }
    return builder.build()
  }

  actual val files: Iterable<File> get() =
    jFile.listFiles()!!.toList().filter(java.io.File::isFile).map(File.Companion::fromJFile)

  actual val subdirectories: Iterable<Directory> get() =
    jFile.listFiles()!!.asList().filter(java.io.File::isDirectory).map(Directory::fromJFile)

  actual fun createFile(name: String): File {
    val newJFile = java.io.File(jFile, name)
    try {
      if (newJFile.createNewFile()) {
        return File.fromJFile(newJFile)
      }
      throw FileAlreadyExistsException(File.fromJFile(newJFile))
    } catch (e: java.io.IOException) {
      throw IOException(e)
    }
  }

  actual fun createSubdirectory(name: String): Directory {
    val newJFile = java.io.File(jFile, name)
    try {
      if (newJFile.mkdir()) {
        return fromJFile(newJFile)
      }
      throw FileAlreadyExistsException(File.fromJFile(newJFile))
    } catch (e: java.io.IOException) {
      throw IOException(e)
    }
  }

  actual companion object {

    actual val workingDirectory: Directory get() = omnia.io.filesystem.Directory(java.io.File("."))

    actual val rootDirectory: Directory get() = omnia.io.filesystem.Directory(java.io.File("/"))

    actual fun fromPath(path: String): Directory = omnia.io.filesystem.Directory(java.io.File(path))

    fun fromJFile(jFile: java.io.File): Directory = omnia.io.filesystem.Directory(jFile)
  }
}
