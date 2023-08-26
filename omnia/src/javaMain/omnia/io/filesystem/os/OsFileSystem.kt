package omnia.io.filesystem.os

import java.io.File as JavaFile
import omnia.io.IOException
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileSystem

actual class OsFileSystem: FileSystem {

  actual override val rootDirectory get() =
    OsDirectory(this, "/")

  actual override val workingDirectory get() =
    OsDirectory(this, ".")

  actual override fun isDirectory(path: String) =
    isFile(JavaFile(path))

  internal fun isDirectory(javaFile: JavaFile) =
    javaFile.isDirectory

  actual override fun isFile(path: String) =
    isFile(JavaFile(path))

  internal fun isFile(javaFile: JavaFile) =
    javaFile.isFile

  actual override fun getDirectory(path: String) =
    OsDirectory(this, path)

  actual override fun getFile(path: String) =
    OsFile(this, path)

  actual override fun createDirectory(path: String): OsDirectory =
    createDirectory(JavaFile(path))

  internal fun createDirectory(javaFile: JavaFile): OsDirectory =
    try {
      if (javaFile.mkdir()) {
        OsDirectory(this, javaFile)
      } else {
        throw FileAlreadyExistsException(OsFile(this, javaFile))
      }
    } catch (e: java.io.IOException) {
      throw IOException(e)
    }

  actual override fun createFile(path: String): OsFile =
    createFile(JavaFile(path))

  internal fun createFile(javaFile: JavaFile): OsFile =
    try {
      if (javaFile.createNewFile()) {
        OsFile(this, javaFile)
      } else {
        throw FileAlreadyExistsException(OsFile(this, javaFile))
      }
    } catch (e: java.io.IOException) {
      throw IOException(e)
    }
}
