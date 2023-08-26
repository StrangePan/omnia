package omnia.io.filesystem.os

import java.io.File as JavaFile
import omnia.data.structure.immutable.ImmutableList
import omnia.io.filesystem.Directory
import omnia.io.filesystem.NotADirectoryException

actual class OsDirectory internal constructor(private val fileSystem: OsFileSystem, private val javaFile: JavaFile):
    Directory {

  internal constructor(fileSystem: OsFileSystem, path: String): this(fileSystem, JavaFile(path))

  init {
    if (!fileSystem.isDirectory(javaFile)) {
      throw NotADirectoryException(javaFile.absolutePath)
    }
  }

  actual override val name: String get() =
    javaFile.absoluteFile.name

  actual override val fullName: String get() =
    javaFile.absolutePath

  actual override val parentDirectory: OsDirectory? get() =
    javaFile.absoluteFile.parentFile?.let { OsDirectory(fileSystem, it) }

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
    javaFile.listFiles()!!.toList().filter(JavaFile::isFile).map { OsFile(fileSystem, it) }

  actual override val subdirectories: Iterable<OsDirectory> get() =
    javaFile.listFiles()!!.asList().filter(JavaFile::isDirectory).map { OsDirectory(fileSystem, it) }

  actual override fun createFile(name: String): OsFile =
    fileSystem.createFile(JavaFile(javaFile, name))

  actual override fun createSubdirectory(name: String): OsDirectory =
    fileSystem.createDirectory(JavaFile(javaFile, name))
}
