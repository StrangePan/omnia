package omnia.io.filesystem.os

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.switchIfEmpty
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.asCompletable
import com.badoo.reaktive.single.doOnAfterSuccess
import com.badoo.reaktive.single.flatMapIterable
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.notNull
import com.badoo.reaktive.single.singleDefer
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.singleOfError
import kotlinx.cinterop.ExperimentalForeignApi
import omnia.cli.out.lineSeparator
import omnia.io.IOException
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.File
import omnia.io.filesystem.NotAFileException
import omnia.platform.swift.WrappedNSError
import omnia.platform.swift.asNSString
import omnia.platform.swift.invokeWithErrorPointer
import omnia.util.reaktive.observable.collectIntoImmutableList
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class)
actual class OsFile internal constructor(override val fileSystem: OsFileSystem, internal var mutablePath: AbsolutePath): File, OsFileSystemObject {

  init {
    if (!fileSystem.fileExistsAt(fullPath)) {
      throw NotAFileException(fullPath.toString())
    }
  }

  actual override val fullPath get() =
    mutablePath

  actual override val name get() =
    fullPath.components.last()

  actual override val directory get() =
    OsDirectory(fileSystem, fullPath - 1)

  actual override fun clearAndWriteLines(lines: Observable<String>): Completable {
    return lines.collectIntoImmutableList()
        .map { it.joinToString(lineSeparator()) }
        .doOnAfterSuccess { path ->
          try {
            invokeWithErrorPointer<NSError, Unit> { errorPtr ->
              path.asNSString().writeToFile(
                fullPath.toString(),
                atomically = true,
                encoding = NSUTF8StringEncoding,
                error = errorPtr)
            }
          } catch (e: Throwable) {
            throw IOException(e)
          }
        }
        .asCompletable()
  }

  actual override fun readLines(): Observable<String> {
    return singleFromFunction {
      NSString.stringWithContentsOfFile(fullPath.toString(), NSUTF8StringEncoding, null)
    }
      .notNull()
      .switchIfEmpty(singleDefer { singleOfError(NotAFileException(fullPath.toString())) })
      .flatMapIterable { it.splitToSequence("\n").asIterable() }
  }

  actual override fun delete() {
    val success = invokeWithErrorPointer { errorPtr ->
      NSFileManager.defaultManager.removeItemAtPath(fullPath.toString(), errorPtr)
    }
    if (!success) {
      throw IOException("Unable to delete file $fullPath")
    }
  }

  actual override fun moveTo(path: AbsolutePath) {
    val success: Boolean
    try {
      success = invokeWithErrorPointer { errorPtr ->
        NSFileManager.defaultManager.moveItemAtPath(fullPath.toString(), path.toString(), errorPtr)
      }
    } catch (e: WrappedNSError) {
      throw IOException("Unable to move file $fullPath to $path", e)
    }
    if (!success) {
      throw IOException("Unable to move file $fullPath to $path")
    }
    mutablePath = path
  }

  actual override fun copyTo(path: AbsolutePath): OsFile {
    val success: Boolean
    try {
      success = invokeWithErrorPointer { errorPtr ->
        NSFileManager.defaultManager.copyItemAtPath(fullPath.toString(), path.toString(), errorPtr)
      }
    } catch (e: WrappedNSError) {
      throw IOException("Unable to copy file $fullPath to $path", e)
    }
    if (!success) {
      throw IOException("Unable to copy file $fullPath to $path")
    }
    return OsFile(fileSystem, path)
  }
}
