package omnia.io

import java.io.File as JFile
import omnia.data.structure.immutable.ImmutableList

actual class Directory private constructor(private val jFile: JFile): FileSystemObject {

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
    jFile.listFiles()!!.toList().filter(JFile::isFile).map(File::fromJFile)

  actual val subdirectories: Iterable<Directory> get() =
    jFile.listFiles()!!.asList().filter(JFile::isDirectory).map(Directory::fromJFile)

  actual fun createFile(name: String): File {
    val newJFile = JFile(jFile, name)
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
    val newJFile = JFile(jFile, name)
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

    actual val workingDirectory: Directory get() = Directory(JFile("."))

    actual val rootDirectory: Directory get() = Directory(JFile("/"))

    actual fun fromPath(path: String): Directory = Directory(JFile(path))

    fun fromJFile(jFile: JFile): Directory = Directory(jFile)
  }
}