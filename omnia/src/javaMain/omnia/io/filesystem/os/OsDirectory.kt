package omnia.io.filesystem.os

import java.io.File as JavaFile
import omnia.data.structure.immutable.ImmutableList
import omnia.io.IOException
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.Directory
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.NotADirectoryException
import omnia.io.filesystem.PathComponent
import omnia.io.filesystem.asAbsolutePath
import omnia.io.filesystem.asPathComponent

actual class OsDirectory internal constructor(override val fileSystem: OsFileSystem, private val javaFile: JavaFile):
    Directory, OsFileSystemObject {

  internal constructor(fileSystem: OsFileSystem, path: String): this(fileSystem, JavaFile(path))

  init {
    if (!fileSystem.isDirectory(javaFile)) {
      throw NotADirectoryException(javaFile.absolutePath)
    }
  }

  actual override val name: PathComponent get() =
    javaFile.absoluteFile.name.asPathComponent()

  actual override val fullPath: AbsolutePath get() =
    javaFile.absolutePath.asAbsolutePath()

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

  actual override val contents: Iterable<OsFileSystemObject> get() =
    javaFile.listFiles()!!
      .asList()
      .mapNotNull {
        if (it.isFile) {
          OsFile(fileSystem, it)
        } else if (it.isDirectory) {
          OsDirectory(fileSystem, it)
        } else {
          null
        }
      }

  actual override val files: Iterable<OsFile> get() =
    javaFile.listFiles()!!.toList().filter(JavaFile::isFile).map { OsFile(fileSystem, it) }

  actual override val subdirectories: Iterable<OsDirectory> get() =
    javaFile.listFiles()!!.asList().filter(JavaFile::isDirectory).map { OsDirectory(fileSystem, it) }

  actual override fun createFile(name: PathComponent): OsFile =
    fileSystem.createFile(JavaFile(javaFile, name.name))

  actual override fun createSubdirectory(name: PathComponent): OsDirectory =
    fileSystem.createDirectory(JavaFile(javaFile, name.name))

  actual override fun delete() {
    if (!javaFile.deleteRecursively()) {
      throw IOException("Unable to fully delete directory $fullPath")
    }
  }

  actual override fun moveTo(path: AbsolutePath) {
    if (!javaFile.renameTo(JavaFile(path.toString()))) {
      throw IOException("Unable to move directory: $fullPath => $path")
    }
  }

  actual override fun copyTo(path: AbsolutePath): OsDirectory {
    try {
      val target = JavaFile(path.toString())
      if (!javaFile.copyRecursively(target, overwrite = false)) {
        throw IOException("Unable to copy directory: $fullPath => $path")
      }
      return OsDirectory(fileSystem, target)
    } catch (e: kotlin.io.NoSuchFileException) {
      throw FileNotFoundException(e)
    } catch (e: kotlin.io.FileAlreadyExistsException) {
      throw FileAlreadyExistsException(path, e)
    } catch (e: java.io.IOException) {
      throw IOException(e)
    }
  }
}
