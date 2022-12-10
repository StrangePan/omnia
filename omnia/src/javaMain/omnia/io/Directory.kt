package omnia.io

import java.io.File as JFile
import omnia.data.structure.immutable.ImmutableList

actual class Directory private constructor(private val jFile: JFile) {

  init {
    if (!jFile.isDirectory) {
      throw NotADirectoryException(jFile.absolutePath)
    }
  }

  actual val name: String get() = jFile.absoluteFile.name

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

  actual companion object {

    actual val workingDirectory: Directory get() = Directory(JFile("."))

    actual val rootDirectory: Directory get() = Directory(JFile("/"))

    actual fun fromPath(path: String): Directory = Directory(JFile(path))

    fun fromJFile(jFile: JFile): Directory = Directory(jFile)
  }
}