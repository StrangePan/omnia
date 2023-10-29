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
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import omnia.cli.out.lineSeparator
import omnia.io.IOException
import omnia.io.filesystem.AbsolutePath
import omnia.io.filesystem.File
import omnia.io.filesystem.NotAFileException
import omnia.platform.swift.asNSString
import omnia.platform.swift.invokeWithErrorPointer
import omnia.util.reaktive.observable.collectIntoImmutableList
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual class OsFile internal constructor(internal val fileSystem: OsFileSystem, actual override val fullPath: AbsolutePath): File {

  init {
    if (!fileSystem.isFile(fullPath)) {
      throw NotAFileException(fullPath.toString())
    }
  }

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
    TODO("Not yet implemented")
  }

  actual override fun moveTo(path: AbsolutePath) {
    TODO("Not yet implemented")
  }

  actual override fun copyTo(path: AbsolutePath): OsFile {
    TODO("Not yet implemented")
  }
}
