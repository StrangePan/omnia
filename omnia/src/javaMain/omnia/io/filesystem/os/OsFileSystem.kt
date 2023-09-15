package omnia.io.filesystem.os

import java.io.File as JavaFile
import omnia.io.IOException
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileSystem

actual class OsFileSystem actual constructor(): FileSystem {

  actual override val rootDirectory get() =
    OsDirectory(this, "/")

  actual override val workingDirectory get() =
    OsDirectory(this, ".")

  actual override fun isDirectory(path: AbsolutePath) =
    isFile(JavaFile(path.toString()))

  internal fun isDirectory(javaFile: JavaFile) =
    javaFile.isDirectory

  actual override fun isFile(path: AbsolutePath) =
    isFile(JavaFile(path.toString()))

  internal fun isFile(javaFile: JavaFile) =
    javaFile.isFile

  actual override fun getDirectory(path: AbsolutePath) =
    OsDirectory(this, path.toString())

  actual override fun getFile(path: AbsolutePath) =
    OsFile(this, path.toString())

  actual fun getResource(path: String): OsFile =
    OsFile(this, ClassLoader.getSystemResource(path).file)

  actual override fun createDirectory(path: AbsolutePath): OsDirectory =
    createDirectory(JavaFile(path.toString()))

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

  actual override fun createFile(path: AbsolutePath): OsFile =
    createFile(JavaFile(path.toString()))

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
