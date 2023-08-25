package omnia.io.filesystem.os

import omnia.data.structure.immutable.ImmutableList
import omnia.io.IOException
import omnia.io.filesystem.Directory
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.NotADirectoryException

actual class OsDirectory private constructor(private val jFile: java.io.File): Directory {

  init {
    if (!jFile.isDirectory) {
      throw NotADirectoryException(jFile.absolutePath)
    }
  }

  actual override val name: String get() = jFile.absoluteFile.name

  actual override val fullName: String get() = jFile.absolutePath

  actual override val parentDirectory: OsDirectory?
    get() = jFile.absoluteFile.parentFile?.let(OsDirectory::fromJFile)

  actual override val parentDirectories: Iterable<OsDirectory> get() {
    val builder = ImmutableList.builder<OsDirectory>()
    var parent = parentDirectory
    while (parent != null) {
      builder.add(parent)
      parent = parent.parentDirectory
    }
    return builder.build()
  }

  actual override val files: Iterable<OsFile> get() =
    jFile.listFiles()!!.toList().filter(java.io.File::isFile).map(OsFile.Companion::fromJFile)

  actual override val subdirectories: Iterable<OsDirectory> get() =
    jFile.listFiles()!!.asList().filter(java.io.File::isDirectory).map(OsDirectory::fromJFile)

  actual override fun createFile(name: String): OsFile {
    val newJFile = java.io.File(jFile, name)
    try {
      if (newJFile.createNewFile()) {
        return OsFile.fromJFile(newJFile)
      }
      throw FileAlreadyExistsException(OsFile.fromJFile(newJFile))
    } catch (e: java.io.IOException) {
      throw IOException(e)
    }
  }

  actual override fun createSubdirectory(name: String): OsDirectory {
    val newJFile = java.io.File(jFile, name)
    try {
      if (newJFile.mkdir()) {
        return fromJFile(newJFile)
      }
      throw FileAlreadyExistsException(OsFile.fromJFile(newJFile))
    } catch (e: java.io.IOException) {
      throw IOException(e)
    }
  }

  actual companion object {

    actual val workingDirectory: OsDirectory get() = OsDirectory(java.io.File("."))

    actual val rootDirectory: OsDirectory get() = OsDirectory(java.io.File("/"))

    actual fun fromPath(path: String): OsDirectory = OsDirectory(java.io.File(path))

    fun fromJFile(jFile: java.io.File): OsDirectory = OsDirectory(jFile)
  }
}
