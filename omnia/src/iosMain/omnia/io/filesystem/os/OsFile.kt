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
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import omnia.cli.out.lineSeparator
import omnia.io.IOException
import omnia.io.filesystem.File
import omnia.io.filesystem.NotAFileException
import omnia.platform.swift.asNSString
import omnia.util.reaktive.observable.collectIntoImmutableList
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.lastPathComponent
import platform.Foundation.stringByDeletingLastPathComponent
import platform.Foundation.stringByDeletingPathExtension
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

actual class OsFile internal constructor(internal val fileSystem: OsFileSystem, private val path: String): File {

  init {
    if (!fileSystem.isFile(path)) {
      throw NotAFileException(path)
    }
  }

  actual override val name get() =
    path.asNSString().lastPathComponent.asNSString().stringByDeletingPathExtension

  actual override val fullName get() = path

  actual override val directory get() =
    OsDirectory(fileSystem, path.asNSString().stringByDeletingLastPathComponent)

  actual override fun clearAndWriteLines(lines: Observable<String>): Completable {
    return lines.collectIntoImmutableList()
        .map { it.joinToString(lineSeparator()) }
        .doOnAfterSuccess {
          memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            it.asNSString().writeToFile(
              path,
              atomically = true,
              encoding = NSUTF8StringEncoding,
              error = error.ptr
            )
            error.value?.also { throw IOException(it.localizedDescription) }
          }
        }
        .asCompletable()
  }

  actual override fun readLines(): Observable<String> {
    return singleFromFunction {
      NSString.stringWithContentsOfFile(path, NSUTF8StringEncoding, null)
    }
      .notNull()
      .switchIfEmpty(singleDefer { singleOfError(NotAFileException(path)) })
      .flatMapIterable { it.splitToSequence("\n").asIterable() }
  }
}
