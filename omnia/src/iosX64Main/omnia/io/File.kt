package omnia.io

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.asCompletable
import com.badoo.reaktive.single.doOnAfterSuccess
import com.badoo.reaktive.single.flatMapIterable
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.singleFromFunction
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.objcPtr
import omnia.cli.out.lineSeparator
import omnia.platform.swift.asNSString
import omnia.util.reaktive.observable.collectIntoImmutableList
import platform.Foundation.NSError
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.lastPathComponent
import platform.Foundation.stringByDeletingLastPathComponent
import platform.Foundation.stringByDeletingPathExtension
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToFile

/** An interface for interfacing with file system files.  */
actual class File private constructor(private val path: String) {
  init {
    if (!isRegularFile(path)) {
      throw NotAFileException(path)
    }
  }

  actual val name get() =
    path.asNSString().lastPathComponent.asNSString().stringByDeletingPathExtension

  actual val directory get() =
    Directory.fromPath(path.asNSString().stringByDeletingLastPathComponent)

  actual fun clearAndWriteLines(lines: Observable<String>): Completable {
    return lines.collectIntoImmutableList()
        .map { it.joinToString(lineSeparator()) }
        .doOnAfterSuccess {
          val error: NSError? = null
          it.asNSString().writeToFile(
              path,
              atomically = true,
              encoding = NSUTF8StringEncoding,
              error = interpretCPointer(error.objcPtr()))
          if (error != null) {
            throw IOException(error.localizedDescription)
          }
        }
        .asCompletable()
  }

  actual fun readLines(): Observable<String> {
    return singleFromFunction {
      val error: NSError? = null
      val contents =
        NSString.Companion.stringWithContentsOfFile(
            path,
            encoding = NSUTF8StringEncoding,
            error = interpretCPointer(error.objcPtr()))
      if (error != null) {
        throw IOException(error.localizedDescription)
      }
      return@singleFromFunction contents!!
    }
        .flatMapIterable { it.splitToSequence("\n").asIterable() }
  }

  actual companion object {

    actual fun fromPath(path: String) = File(path)

    fun isRegularFile(path: String): Boolean {
      val isDirectory = false
      val exists =
        NSFileManager.defaultManager.fileExistsAtPath(
            path, isDirectory= interpretCPointer(isDirectory.objcPtr()))
      return exists && !isDirectory
    }
  }
}