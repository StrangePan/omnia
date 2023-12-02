package omnia.io.filesystem.os

import java.io.File as JavaFile
import omnia.data.cache.Memoized
import omnia.io.IOException
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.FileAlreadyExistsException
import omnia.io.filesystem.FileNotFoundException
import omnia.io.filesystem.FileSystem

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class OsFileSystem private constructor(): FileSystem {

  actual override val rootDirectory get() =
    OsDirectory(this, "/")

  actual override val workingDirectory get() =
    OsDirectory(this, ".")

  actual override fun objectExistsAt(path: AbsolutePath): Boolean =
    JavaFile(path.toString()).exists()

  actual override fun directoryExistsAt(path: AbsolutePath) =
    isDirectory(JavaFile(path.toString()))

  internal fun isDirectory(javaFile: JavaFile) =
    javaFile.isDirectory

  actual override fun fileExistsAt(path: AbsolutePath) =
    isFile(JavaFile(path.toString()))

  internal fun isFile(javaFile: JavaFile) =
    javaFile.isFile

  actual override fun getObjectAt(path: AbsolutePath) =
    JavaFile(path.toString()).let { javaFile ->
      if (javaFile.isDirectory) {
        OsDirectory(this, path.toString())
      } else if (javaFile.isFile) {
        OsFile(this, path.toString())
      } else {
        throw FileNotFoundException(path.toString())
      }
    }

  actual override fun getDirectoryAt(path: AbsolutePath) =
    OsDirectory(this, path.toString())

  actual override fun getFileAt(path: AbsolutePath) =
    OsFile(this, path.toString())

  private fun getResourcePath(path: AbsolutePath) =
    ClassLoader.getSystemResource(path.removePrefix(AbsolutePath.empty()).toString()).file

  actual fun getResourceFileAt(path: AbsolutePath): OsFile =
    OsFile(this, getResourcePath(path))

  actual fun getResourceDirectoryAt(path: AbsolutePath): OsDirectory =
    OsDirectory(this, getResourcePath(path))

  actual fun getResourceObjectAt(path: AbsolutePath): OsFileSystemObject {
    val resourcePath = getResourcePath(path)
    return JavaFile(resourcePath).let { javaFile ->
      if (javaFile.isDirectory) {
        OsDirectory(this, resourcePath)
      } else if (javaFile.isFile) {
        OsFile(this, resourcePath)
      } else {
        throw FileNotFoundException(resourcePath)
      }
    }
  }

  actual override fun createDirectoryAt(path: AbsolutePath): OsDirectory =
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

  actual override fun createFileAt(path: AbsolutePath): OsFile =
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

  actual companion object {
    private val memoizedInstance = Memoized.memoize(::OsFileSystem)

    actual val instance get() = memoizedInstance.value
  }
}
